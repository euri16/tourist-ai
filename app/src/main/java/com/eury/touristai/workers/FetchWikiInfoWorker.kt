package com.eury.touristai.workers

import androidx.work.Worker
import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.repository.remote.services.GoogleCloudServiceGenerator
import com.eury.touristai.repository.remote.services.PlacesServiceGenerator
import com.eury.touristai.repository.remote.services.WikipediaServiceGenerator
import com.eury.touristai.utils.Loggable.Companion.log

/**
 * Created by euryperez on 5/22/18.
 * Property of Instacarro.com
 */
class FetchWikiInfoWorker : Worker() {

    private val placesRepository = PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
            PlacesServiceGenerator.createService(PlacesRequests::class.java),
            WikipediaServiceGenerator.createService(WikiRequests::class.java))

    override fun doWork(): WorkerResult {

        val placeId = inputData.getString(PLACE_ID_KEY, null)
        val placeName = inputData.getString(PLACE_NAME_KEY, null)

        if(placeId is String && placeName is String) {
            val place = placesRepository.getPlace(placeId)
            placesRepository.getPlaceWikiInfo(placeId, placeName) { response, isError, _ ->
                if(response == null || isError) {
                    place?.wikiPageTitle?.let {
                        placesRepository.getPlaceWikiInfo(placeId, it){ response, isError, t ->
                            log.d("wiki try")
                        }
                    }
                }
            }
        }

        return WorkerResult.SUCCESS
    }

    companion object {
        const val PLACE_ID_KEY = "PLACE_ID_KEY"
        const val PLACE_NAME_KEY = "PLACE_NAME_KEY"
    }
}