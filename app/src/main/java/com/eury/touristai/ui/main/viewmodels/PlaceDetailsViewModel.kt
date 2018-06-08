package com.eury.touristai.ui.main.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.ktx.OneTimeWorkRequestBuilder
import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.repository.remote.services.GoogleCloudServiceGenerator
import com.eury.touristai.repository.remote.services.PlacesServiceGenerator
import com.eury.touristai.repository.remote.services.WikipediaServiceGenerator
import com.eury.touristai.ui.main.models.PlaceDetailsModel
import com.eury.touristai.workers.FetchWikiInfoWorker

/**
 * Created by euryperez on 5/17/18.
 * Property of Instacarro.com
 */
class PlaceDetailsViewModel : ViewModel() {

    var placeId = ""

    var model: PlaceDetailsModel = PlaceDetailsModel()

    private var placesRepository: PlacesRepository =
            PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
                    PlacesServiceGenerator.createService(PlacesRequests::class.java),
                    WikipediaServiceGenerator.createService(WikiRequests::class.java))

    init {
    }

    fun getPlace(placeId:String) : LiveData<Place> {
        if(this.placeId == placeId) {
            model.place?.let {
                return it
            }
        }

        this.placeId = placeId
        model.place = placesRepository.getPlaceById(placeId)
        return model.place!!
    }

    fun fetchPlaceDetailInfo(placeId: String, placeName: String) {
        val workerData = Data.Builder()
                .putString(FetchWikiInfoWorker.PLACE_ID_KEY, placeId)
                .putString(FetchWikiInfoWorker.PLACE_NAME_KEY, placeName)
                .build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<FetchWikiInfoWorker>()
                .setInputData(workerData).build()

        WorkManager.getInstance()
                .enqueue(oneTimeWorkRequest)
    }
}