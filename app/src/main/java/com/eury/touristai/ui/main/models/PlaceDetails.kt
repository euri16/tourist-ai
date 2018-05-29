package com.eury.touristai.ui.main.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.eury.touristai.repository.local.entities.Place

/**
 * Created by euryperez on 5/21/18.
 * Property of Instacarro.com
 */
data class PlaceDetails (
        var isLoading:MutableLiveData<Boolean> = MutableLiveData(),
        var isError:MutableLiveData<Boolean> = MutableLiveData(),
        var place: LiveData<Place>? = null
)