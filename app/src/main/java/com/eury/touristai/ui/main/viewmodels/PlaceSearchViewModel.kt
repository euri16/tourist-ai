package com.eury.touristai.ui.main.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
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
import com.eury.touristai.utils.toBase64
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PlaceSearchViewModel(application: Application) : AndroidViewModel(application) {

    private var placesRepository: PlacesRepository =
            PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
                PlacesServiceGenerator.createService(PlacesRequests::class.java),
                    WikipediaServiceGenerator.createService(WikiRequests::class.java))

    var model:PlaceSearch = PlaceSearch()

    init {
        model.isError.value = false
        model.isLoading.value = false
    }

    fun getPlaces(): LiveData<PlaceSearchResponse> {
        return model.places
    }


    fun submitBitmapForAnalysis(bitmap: Bitmap) {
        model.isLoading.value = true
        doAsync {
            val base64 = bitmap.toBase64()
            placesRepository.getPlaceDetailsWithImage(base64) { response, isError, _ ->
                uiThread {
                    if(response?.results !is List<PlaceSearchResponse.Result> || response.results!!.isEmpty()) {
                        model.isError.value = true
                    } else {
                        model.isLoading.value = false
                        model.places.value = response
                        model.isError.value = isError
                    }
                }
            }
        }
    }
}