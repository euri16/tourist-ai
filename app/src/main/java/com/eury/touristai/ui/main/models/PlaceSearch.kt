package com.eury.touristai.ui.main.models

import android.arch.lifecycle.MutableLiveData
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.utils.SingleLiveEvent

/**
 * Created by euryperez on 5/21/18.
 * Property of Instacarro.com
 */
data class PlaceSearch (var isLoading:MutableLiveData<Boolean> = MutableLiveData(),
                        var isError:MutableLiveData<Boolean> = MutableLiveData(),
                        var places: SingleLiveEvent<PlaceSearchResponse> = SingleLiveEvent())