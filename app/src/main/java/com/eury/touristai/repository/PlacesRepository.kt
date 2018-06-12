package com.eury.touristai.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.eury.touristai.R
import com.eury.touristai.TouristAI
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.remote.models.*
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.utils.perform
import com.eury.touristai.utils.stripAccents
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Response
import java.util.*
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

    fun getVisionDetailsWithImage(base64Image: String, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        getVisionResultsWithImage(base64Image) { placeDescription, _, _ ->
            placeDescription?.name?.let {
                getPlaceDetailsWithPlaceDescription(placeDescription) { r2, err2, t2 ->
                    callback(r2, err2, t2)
                }
                return@getVisionResultsWithImage
            }

            callback(null, true, null)
        }
    }

    fun getPlaceDetailsWithName(placeName: String, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        val placeDescription = PlaceDescription(name = placeName, wikiPageTitle = null, webEntityTitle = null)
        getPlaceDetailsWithPlaceDescription(placeDescription) { response, isError, t ->
            callback(response, isError, t)
        }
    }

    private fun getPlaceDetailsWithPlaceDescription(placeDescription: PlaceDescription, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        doAsync {
            val existingPlace = getPlaceByVisionName(placeDescription.name?.toLowerCase() ?: "")
            existingPlace?.let {
                uiThread {
                    val placeSearchResponse = PlaceSearchResponse(placeDescription.name!!, existingPlace)
                    callback(placeSearchResponse, false, null)
                }
                return@doAsync
            }

            placesWebService.getPlace(placeDescription.name ?: "").perform { r, err, t ->
                r?.results?.getOrNull(0)?.let {
                    savePlace(it, placeDescription)
                    callback(r, err, t)
                    return@perform
                }

                callback(null, true, null)
            }
        }
    }

    fun getPlaceDetailsWithPlaceId(placeId: String) : Response<PlaceDetailsResponse>? {
        return placesWebService.getPlaceDetails(placeId).execute()
    }

    private fun getVisionResultsWithImage(base64Image: String,
                                          callback: (response: PlaceDescription?, isError: Boolean, t: Throwable?) -> Unit) {
        val visionRequest = VisionLandmarkRequest(base64Image, SEARCH_FEATURES)

        visionWebService.submitImageForAnalysis(visionRequest).perform { response, isError, throwable ->
            val placeName = getPlaceNameByVisionResult(response)
            val webEntityTitle = getWebEntityTitleByVisionResult(response)
            val wikiTitle = getWikiTitleByVisionResult(response)

            val placeDescription = PlaceDescription(placeName, wikiTitle, webEntityTitle)
            callback(placeDescription, isError, throwable)
        }
    }

    private fun getWebEntityTitleByVisionResult(response: VisionResponse?): String? {
        val webEntities = response?.responses?.filter { it.webDetection?.webEntities?.isEmpty() == false }
        webEntities?.getOrNull(0)?.let {
            return webEntities[0].webDetection?.webEntities?.get(0)?.description
        }
        return ""
    }

    private fun getPlaceNameByVisionResult(response: VisionResponse?): String? {
        var placeName: String? = null

        val landmarkAnnotations = response?.responses?.filter { it.landmarkAnnotations?.isEmpty() == false }
        if (landmarkAnnotations?.isEmpty() == false) {
            placeName = landmarkAnnotations[0].landmarkAnnotations?.get(0)?.description
        }

        val guessLabels = response?.responses?.filter { it.webDetection?.bestGuessLabels?.isEmpty() == false }
        if (placeName == null && guessLabels?.isEmpty() == false) {
            placeName = guessLabels[0].webDetection?.bestGuessLabels?.get(0)?.label
        }

        val webEntities = response?.responses?.filter { it.webDetection?.webEntities?.isEmpty() == false }
        if (placeName == null && webEntities?.isEmpty() == false) {
            placeName = webEntities[0].webDetection?.webEntities?.get(0)?.description
        }

        return placeName
    }

    private fun getWikiTitleByVisionResult(response: VisionResponse?): String? {
        val pageWithMatchingImgs = response?.responses?.filter { it.webDetection?.pagesWithMatchingImages?.isEmpty() == false }

        if (pageWithMatchingImgs?.isEmpty() == true) {
            return null
        }

        val wikiPreUrl = TouristAI.applicationContext().getString(R.string.wikipedia_pre_url)
        val wikiPages = pageWithMatchingImgs?.get(0)?.webDetection?.pagesWithMatchingImages?.filter {
            it.url.contains(wikiPreUrl) && !it.url.endsWith(".jpg") && !it.url.endsWith(".png")
        }

        return wikiPages?.getOrNull(0)?.url?.replace(wikiPreUrl, "")
    }

    fun getPlaceWikiInfoSync(placeId: String, placeName: String): Response<WikiResponse> {
        return wikiWebService.getPlaceInfo(placeName.stripAccents()).execute()
    }

    fun processWikiResponse(response: WikiResponse?, placeId: String) {
        doAsync {
            val place = TouristAI.database?.placeDao()?.loadPlace(placeId)
            place?.let {
                val extract = response?.extract?.let { it } ?: ""
                place.summaryInfo = extract
                place.name = response?.displayTitle ?: ""
                TouristAI.database?.placeDao()?.update(it)
            }
        }

    }

    fun processPlacesDetails(response: PlaceDetailsResponse?, placeId: String) {
        doAsync {
            response?.result?.let { result ->
                val place = TouristAI.database?.placeDao()?.loadPlace(placeId)
                place?.let {
                    place.phoneNumber = result.international_phone_number
                    place.openingHours = result.openingHours
                    place.utcOffset = result.utcOffset
                    TouristAI.database?.placeDao()?.update(place)
                }
            }
        }
    }

    fun getLivePlaceByPlaceId(placeId: String): LiveData<Place> {
        return TouristAI.database?.placeDao()?.loadPlaceById(placeId)!!
    }

    private fun getPlaceByVisionName(placeName: String) = TouristAI.database?.placeDao()?.loadPlaceByVisionName(placeName.toLowerCase())

    fun getPlaceByPlaceId(placeId: String?): Place? {
        return placeId?.let { TouristAI.database?.placeDao()?.loadPlace(it) }
    }

    private fun savePlace(placeResult: PlaceSearchResponse.Result, placeDescription: PlaceDescription) {
        doAsync {
            val placeExists = getPlaceByPlaceId(placeResult.placeId!!) != null
            if (!placeExists) {
                val place = placeResult.buildPlace(placeDescription)
                TouristAI.database?.placeDao()?.save(place)
            }
        }
    }

    fun getLiveAllPlaces(): LiveData<List<Place>>? {
        return TouristAI.database?.placeDao()?.loadAll()
    }

    data class PlaceDescription(val name: String?, val wikiPageTitle: String?, val webEntityTitle: String?)

    companion object {
        private val SEARCH_FEATURES = arrayOf("LANDMARK_DETECTION", "WEB_DETECTION")
    }
}