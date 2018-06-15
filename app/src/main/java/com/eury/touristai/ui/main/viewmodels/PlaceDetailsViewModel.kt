package com.eury.touristai.ui.main.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.util.StringUtil
import android.text.TextUtils
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.ktx.OneTimeWorkRequestBuilder
import com.eury.touristai.R
import com.eury.touristai.TouristAI
import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.remote.models.PlaceDetailsResponse
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.repository.remote.services.GoogleCloudServiceGenerator
import com.eury.touristai.repository.remote.services.PlacesServiceGenerator
import com.eury.touristai.repository.remote.services.WikipediaServiceGenerator
import com.eury.touristai.ui.main.models.PlaceDetailsModel
import com.eury.touristai.utils.Credentials
import com.eury.touristai.workers.FetchPlaceDetailsWorker
import com.eury.touristai.workers.FetchWikiInfoWorker
import org.jetbrains.anko.doAsync
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

/**
 * Created by euryperez on 5/17/18.
 * Property of Instacarro.com
 */
class PlaceDetailsViewModel(application: Application) : AndroidViewModel(application) {

    var model: PlaceDetailsModel = PlaceDetailsModel()
    var placeId = ""

    private var placesRepository: PlacesRepository = PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
                    PlacesServiceGenerator.createService(PlacesRequests::class.java),
                    WikipediaServiceGenerator.createService(WikiRequests::class.java))

    init {
    }

    fun getPhotoUrl(references: List<String>?): String {
        references?.getOrNull(0)?.let { photoRef ->
            val context = TouristAI.applicationContext()
            return context.getString(R.string.google_photo_reference, photoRef, 1024, 1024,
                    Credentials.placesApiKey)
        }
        return ""
    }

    fun getPlace(placeId: String): LiveData<Place>? {
        if (this.placeId == placeId) {
            return model.place
        }

        this.placeId = placeId
        model.place = placesRepository.getLivePlaceByPlaceId(placeId)
        return model.place
    }

    fun fetchWikiDetailInfo(placeId: String, workTag:String = FETCH_WIKI_DATA_WITH_NAME) {
        doAsync {
            val place = placesRepository.getPlaceByPlaceId(placeId)

            place?.name?.let {
                val wikiByNameWork = OneTimeWorkRequestBuilder<FetchWikiInfoWorker>()
                        .setInputData(buildWikiWorkerData(placeId, it))
                        .addTag(workTag)
                        .build()

                val placeDetailsWork = OneTimeWorkRequestBuilder<FetchPlaceDetailsWorker>()
                        .setInputData(buildPlaceDetailsWorkerData(placeId))
                        .build()
                WorkManager.getInstance().enqueue(wikiByNameWork, placeDetailsWork)
            }
        }
    }

    private fun buildWikiWorkerData(placeId:String, placeName:String) : Data {
        return Data.Builder()
                .putString(FetchWikiInfoWorker.PLACE_ID_KEY, placeId)
                .putString(FetchWikiInfoWorker.PLACE_NAME_KEY, placeName)
                .build()
    }

    private fun buildPlaceDetailsWorkerData(placeId:String) : Data {
        return Data.Builder()
                .putString(FetchPlaceDetailsWorker.PLACE_ID_KEY, placeId)
                .build()
    }

    fun isOpen(openingHours: PlaceDetailsResponse.OpeningHours?) : Boolean {

        if(openingHours == null) return true

        val utcOffset = model.place?.value?.utcOffset?.div(60)
        val format = DateTimeFormat.forPattern("hhmm")
        val currentDateTime = DateTime(DateTimeZone.forOffsetHours(utcOffset ?: 0))

        val militaryTime = format.print(currentDateTime).toInt()
        val dayOfWeek = currentDateTime.dayOfWeek().get()

        if(openingHours.periods?.count() == 1 &&
                openingHours.periods?.getOrNull(0)?.open?.time?.equals("0000") == true) {
            return true
        }

        val period = openingHours.periods?.filter { it.open?.day?.equals(dayOfWeek) == true }

        if(period?.isEmpty() == true || (period?.getOrNull(0)?.open?.time?.toInt() ?: 0) < militaryTime ||
                (period?.getOrNull(0)?.close?.time?.toInt() ?: 0) > militaryTime) {
            return false
        }

        return true
    }

    fun getOpenStatus(openingHours: PlaceDetailsResponse.OpeningHours?) : String {
        if(isOpen(openingHours)) {
            return TouristAI.applicationContext().getString(R.string.open_text)
        }

        return TouristAI.applicationContext().getString(R.string.closed_text)
    }

    companion object {
        const val FETCH_WIKI_DATA_WITH_NAME = "001"
    }
}