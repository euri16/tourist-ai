package com.eury.touristai.repository

import android.arch.lifecycle.LiveData
import com.eury.touristai.R
import com.eury.touristai.TouristAI
import com.eury.touristai.repository.local.AppDb
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.remote.models.VisionLandmarkRequest
import com.eury.touristai.repository.remote.models.VisionResponse
import com.eury.touristai.repository.remote.models.WikiResponse
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.utils.Loggable.Companion.log
import com.eury.touristai.utils.perform
import com.eury.touristai.utils.stripAccents
import org.jetbrains.anko.doAsync
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */

@Singleton
class PlacesRepository @Inject constructor(private val visionWebService: VisionRequests,
                                           private val placesWebService: PlacesRequests,
                                           private val wikiWebService: WikiRequests) {

    fun getPlaceDetailsWithImage(base64Image: String, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        getVisionResultsWithImage(base64Image) { placeDescription, _, _ ->
            placeDescription?.name?.let {
                getPlaceDetailsWithName(placeDescription) { r2, err2, t2 ->
                    callback(r2, err2, t2)
                }
            }
        }
    }

    private fun getPlaceDetailsWithName(placeDescription: PlaceDescription, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        doAsync {
            val existingPlace = TouristAI.database?.placeDao()?.loadPlaceByOriginalName(placeDescription.name?.toLowerCase()!!)
            if(existingPlace == null) {
                placesWebService.getPlaceDetails(placeDescription.name ?: "", PLACES_API_KEY).perform { r, err, t ->
                    r?.results?.let {
                        savePlace(it[0], placeDescription)
                        callback(r, err, t)
                    }
                }
            } else {
                val placeSearchResponse = PlaceSearchResponse(placeName = placeDescription.name!!, existingPlace = existingPlace)
                callback(placeSearchResponse, false, null)
            }
        }
    }

    private fun getVisionResultsWithImage(base64Image: String, callback: (response: PlaceDescription?, isError: Boolean, t: Throwable?) -> Unit) {
        val visionRequest = VisionLandmarkRequest(base64Image, SEARCH_FEATURES)
        visionWebService.submitImageForAnalysis(visionRequest, VISION_API_KEY).perform { response, isError, throwable ->
            val placeDescription = PlaceDescription(name = getPlaceNameByVisionResult(response),
                    wikiPageTitle = getWikiTitleByVisionResult(response))

            callback(placeDescription, isError, throwable)
        }
    }

    // TODO: improve with map
    private fun getPlaceNameByVisionResult(response: VisionResponse?) : String? {
        val landmarkAnnotations = response?.responses?.filter { it.landmarkAnnotations?.isEmpty() == false }
        val guessLabels = response?.responses?.filter { it.webDetection?.bestGuessLabels?.isEmpty() == false }
        val webEntities = response?.responses?.filter { it.webDetection?.webEntities?.isEmpty() == false }

        var placeName:String? = null

        if(landmarkAnnotations?.isEmpty() == false) {
            placeName = landmarkAnnotations[0].landmarkAnnotations?.get(0)?.description
        }

        if(placeName == null && guessLabels?.isEmpty() == false) {
            placeName = guessLabels[0].webDetection?.bestGuessLabels?.get(0)?.label
        }

        if(placeName == null && webEntities?.isEmpty() == false) {
            placeName = webEntities[0].webDetection?.webEntities?.get(0)?.description
        }

        return placeName
    }

    private fun getWikiTitleByVisionResult(response: VisionResponse?) : String? {
        val pageWithMatchingImgs = response?.responses?.filter { it.webDetection?.pagesWithMatchingImages?.isEmpty() == false }

        if(pageWithMatchingImgs?.isEmpty() == false) {
            val wikiPreUrl = TouristAI.applicationContext().getString(R.string.wikipedia_pre_url)
            val wikiPages = pageWithMatchingImgs[0].webDetection?.pagesWithMatchingImages?.filter {
                it.url.contains(wikiPreUrl) && !it.url.endsWith(".jpg") && !it.url.endsWith(".png")
            }

            return wikiPages?.getOrNull(0)?.url?.replace(wikiPreUrl, "")
        }

        return null
    }

    fun getPlaceWikiInfo(placeId:String, placeName:String, callback: (WikiResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        wikiWebService.getPlaceInfo(placeName.stripAccents()).perform { response, isError, t ->
            doAsync {
                val place = TouristAI.database?.placeDao()?.loadPlace(placeId)
                place?.let {
                    val extract = response?.extract?.let { it } ?: ""
                    place.summaryInfo = extract
                    place.name = response?.displayTitle ?: ""
                    TouristAI.database?.placeDao()?.update(it)
                    callback(response, isError, t)
                }
            }
        }
    }

    fun getPlaceById(placeId: String): LiveData<Place> {
        return TouristAI.database?.placeDao()?.loadPlaceById(placeId)!!
    }

    fun getPlace(placeId: String?): Place? {
        return placeId?.let { TouristAI.database?.placeDao()?.loadPlace(it) }
    }

    fun getAllPlaces(): List<Place> {
        return TouristAI.database?.placeDao()?.loadAll() ?: listOf()
    }

    private fun savePlace(placeResult: PlaceSearchResponse.Result, placeDescription: PlaceDescription) {
        doAsync {
            val placeExists= getPlace(placeResult.placeId!!) != null
            if(!placeExists) {
                val place = placeResult.buildPlace(placeDescription)
                TouristAI.database?.placeDao()?.save(place)
            }
        }
    }

    data class PlaceDescription(val name:String?, val wikiPageTitle:String?)


    companion object {
        private val SEARCH_FEATURES = arrayOf("LANDMARK_DETECTION", "WEB_DETECTION")
        private const val VISION_API_KEY = "AIzaSyDr-GdBogWl_2t4etWIi1CQLpGbvrBP7mY"
        private const val PLACES_API_KEY = "AIzaSyATG__L98EAthNwQ3fK8zDDrlk4-bWU-wE"
    }
}