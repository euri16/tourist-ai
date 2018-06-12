package com.eury.touristai.workers

import android.text.TextUtils
import androidx.work.Worker
import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.repository.remote.services.GoogleCloudServiceGenerator
import com.eury.touristai.repository.remote.services.PlacesServiceGenerator
import com.eury.touristai.repository.remote.services.WikipediaServiceGenerator

/**
 * Created by euryperez on 5/22/18.
 * Property of Instacarro.com
 */
class FetchPlaceDetailsWorker : Worker() {

    private val placesRepository = PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
            PlacesServiceGenerator.createService(PlacesRequests::class.java),
            WikipediaServiceGenerator.createService(WikiRequests::class.java))

    override fun doWork(): WorkerResult {
        val placeId = inputData.getString(PLACE_ID_KEY, null)

        val response = placesRepository.getPlaceDetailsWithPlaceId(placeId)

        if(TextUtils.isEmpty(placeId) || response?.isSuccessful == false) {
            return WorkerResult.FAILURE
        }

        placesRepository.processPlacesDetails(response?.body(), placeId)

        return WorkerResult.SUCCESS
    }

    companion object {
        const val PLACE_ID_KEY = "PLACE_ID_KEY"
    }
}