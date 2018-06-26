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
class FetchWikiInfoWorker : Worker() {

    private val placesRepository = PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
            PlacesServiceGenerator.createService(PlacesRequests::class.java),
            WikipediaServiceGenerator.createService(WikiRequests::class.java))

    override fun doWork(): Worker.Result {
        val placeId = inputData.getString(PLACE_ID_KEY, null)
        val placeName = inputData.getString(PLACE_NAME_KEY, null)

        if(!TextUtils.isEmpty(placeId) && !TextUtils.isEmpty(placeName)) {
            val queryResponse = placesRepository.getPlaceWikiInfoQuerySync(placeName)

            if(queryResponse.isSuccessful) {
                val wikiPageTitle = queryResponse.body()?.query?.search?.getOrNull(0)?.title
                if(wikiPageTitle?.isNotEmpty() == true) {
                    val pageResponse = placesRepository.getPlaceWikiInfoSync(wikiPageTitle)
                    if(pageResponse.isSuccessful) {
                        placesRepository.processWikiResponse(pageResponse.body(), placeId)
                        return Worker.Result.SUCCESS
                    }
                }
            }
        }

        processPlaceNotFound(placeId)
        return Worker.Result.FAILURE
    }

    private fun processPlaceNotFound(placeId:String) {
        placesRepository.processWikiResponse(null, placeId)
    }

    companion object {
        const val PLACE_ID_KEY = "PLACE_ID_KEY"
        const val PLACE_NAME_KEY = "PLACE_NAME_KEY"
    }
}