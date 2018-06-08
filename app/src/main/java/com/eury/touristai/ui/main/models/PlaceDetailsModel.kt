package com.eury.touristai.ui.main.models

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.eury.touristai.repository.local.entities.Place

/**
 * Created by euryperez on 5/21/18.
 * Property of Instacarro.com
 */
class PlaceDetailsModel {
    var place: LiveData<Place>? = null

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isError: MutableLiveData<Boolean> = MutableLiveData()
}