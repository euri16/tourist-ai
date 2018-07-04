package com.eury.touristai.repository

import android.arch.lifecycle.LiveData
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

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */

class PlacesRepository constructor(private val visionWebService: VisionRequests,
                                           private val placesWebService: PlacesRequests,
                                           private val wikiWebService: WikiRequests) {

    fun getVisionDetailsWithImage(base64Image: String, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        getVisionResultsWithImage(base64Image) { placeName, _, _ ->
            placeName?.let {
                getPlaceDetailsWithPlaceName(placeName) { r2, err2, t2 ->
                    callback(r2, err2, t2)
                }
                return@getVisionResultsWithImage
            }
            callback(null, true, null)
        }
    }

    fun getPlaceDetailsWithName(placeName: String, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        getPlaceDetailsWithPlaceName(placeName) { response, isError, t ->
            callback(response, isError, t)
        }
    }

    private fun getPlaceDetailsWithPlaceName(placeName: String, callback: (PlaceSearchResponse?, isError: Boolean, t: Throwable?) -> Unit) {
        doAsync {
            val existingPlace = getPlaceByVisionName(placeName.toLowerCase())
            existingPlace?.let {
                uiThread {
                    val placeSearchResponse = PlaceSearchResponse(placeName, existingPlace)
                    callback(placeSearchResponse, false, null)
                }
                return@doAsync
            }

            placesWebService.getPlace(placeName).perform { r, err, t ->
                r?.results?.getOrNull(0)?.let {
                    savePlace(it, placeName)
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
                                          callback: (placeName: String?, isError: Boolean, t: Throwable?) -> Unit) {
        val visionRequest = VisionLandmarkRequest(base64Image, SEARCH_FEATURES)

        visionWebService.submitImageForAnalysis(visionRequest).perform { response, isError, throwable ->
            val placeName = getPlaceNameByVisionResult(response)

            callback(placeName, isError, throwable)
        }
    }

    private fun getWebEntityByVisionResult(response: VisionResponse?): VisionResponse.WebEntity? {
        val webEntities = response?.responses?.filter { it.webDetection?.webEntities?.isEmpty() == false }
        webEntities?.getOrNull(0)?.let {
            return webEntities[0].webDetection?.webEntities?.get(0)
        }
        return null
    }

    private fun getPlaceNameByVisionResult(response: VisionResponse?): String? {
        var placeName: String? = null

        val webEntity = getWebEntityByVisionResult(response)
        webEntity?.let {
            if(webEntity.score >= WEB_ENTITY_MIN_SCORE) {
                placeName = webEntity.description
            }
        }

        val guessLabels = response?.responses?.filter { it.webDetection?.bestGuessLabels?.isEmpty() == false }
        if (placeName == null && guessLabels?.isEmpty() == false) {
            placeName = guessLabels[0].webDetection?.bestGuessLabels?.get(0)?.label
        }

        return placeName
    }

    fun getPlaceWikiInfoQuerySync(placeName:String): Response<WikiQueryResponse> {
        return wikiWebService.getQueryResults(queryText = placeName).execute()
    }

    fun getPlaceWikiInfoSync(placeName: String): Response<WikiResponse> {
        return wikiWebService.getPlaceInfo(placeName.stripAccents()).execute()
    }

    fun processWikiResponse(response: WikiResponse?, placeId: String) {
        doAsync {
            val place = TouristAI.database?.placeDao()?.loadPlace(placeId)
            place?.let {
                val extract = response?.extract?.let { it } ?: "-"
                place.summaryInfo = extract
                //place.name = response?.displayTitle ?: ""
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

    private fun savePlace(placeResult: PlaceSearchResponse.Result, placeName: String) {
        doAsync {
            val placeExists = getPlaceByPlaceId(placeResult.placeId!!) != null
            if (!placeExists) {
                val place = placeResult.buildPlace(placeName)
                TouristAI.database?.placeDao()?.save(place)
            }
        }
    }

    fun getLiveAllPlaces(): LiveData<List<Place>>? {
        return TouristAI.database?.placeDao()?.loadAll()
    }

    companion object {
        private val SEARCH_FEATURES = arrayOf("WEB_DETECTION")
        private val WEB_ENTITY_MIN_SCORE = 1.3
    }
}