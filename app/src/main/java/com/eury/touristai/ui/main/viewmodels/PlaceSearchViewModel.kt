package com.eury.touristai.ui.main.viewmodels

import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.graphics.Bitmap
import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.repository.remote.services.GoogleCloudServiceGenerator
import com.eury.touristai.repository.remote.services.PlacesServiceGenerator
import com.eury.touristai.repository.remote.services.WikipediaServiceGenerator
import com.eury.touristai.ui.main.models.PlaceSearch
import com.eury.touristai.utils.SingleLiveEvent
import com.eury.touristai.utils.toBase64
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PlaceSearchViewModel(application: Application) : AndroidViewModel(application) {

    private var placesRepository: PlacesRepository =
            PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
                    PlacesServiceGenerator.createService(PlacesRequests::class.java),
                    WikipediaServiceGenerator.createService(WikiRequests::class.java))

    var model: PlaceSearch = PlaceSearch()
    var isTextSearchDialogShowing = false

    init {
        model.isError.value = false
        model.isLoading.value = false
    }

    fun getPlaces(): SingleLiveEvent<PlaceSearchResponse> {
        return model.places
    }

    fun submitBitmapForAnalysis(bitmap: Bitmap) {
        model.isLoading.value = true
        doAsync {
            val base64 = bitmap.toBase64()

            placesRepository.getVisionDetailsWithImage(base64) { response, isError, _ ->
                uiThread {
                    processPlaceResponse(response, isError)
                }
            }
        }
    }

    fun submitPlaceName(placeName: String) {
        model.isLoading.value = true
        placesRepository.getPlaceDetailsWithName(placeName) { response, isError, _ ->
            processPlaceResponse(response, isError)
        }
    }

    private fun processPlaceResponse(response: PlaceSearchResponse?, isError:Boolean) {
        val isResponseValid = response?.results is List<PlaceSearchResponse.Result> &&
                response.results?.isEmpty() == false

        if (!isResponseValid) {
            model.isError.value = true
            model.isLoading.value = false
            return
        }

        model.isLoading.value = false
        model.places.value = response
        model.isError.value = isError
    }

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 0
        val REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}