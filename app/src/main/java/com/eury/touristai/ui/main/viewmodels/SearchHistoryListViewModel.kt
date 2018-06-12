package com.eury.touristai.ui.main.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.remote.requests.PlacesRequests
import com.eury.touristai.repository.remote.requests.VisionRequests
import com.eury.touristai.repository.remote.requests.WikiRequests
import com.eury.touristai.repository.remote.services.GoogleCloudServiceGenerator
import com.eury.touristai.repository.remote.services.PlacesServiceGenerator
import com.eury.touristai.repository.remote.services.WikipediaServiceGenerator

class SearchHistoryListViewModel : ViewModel() {

    private var placesRepository: PlacesRepository = PlacesRepository(GoogleCloudServiceGenerator.createService(VisionRequests::class.java),
            PlacesServiceGenerator.createService(PlacesRequests::class.java),
            WikipediaServiceGenerator.createService(WikiRequests::class.java))

    fun getPlaces() : LiveData<List<Place>>? {
        return placesRepository.getLiveAllPlaces()
    }
}
