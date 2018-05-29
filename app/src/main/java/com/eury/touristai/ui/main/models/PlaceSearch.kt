package com.eury.touristai.ui.main.models

import android.arch.lifecycle.MutableLiveData
import com.eury.touristai.repository.remote.models.PlaceSearchResponse

/**
 * Created by euryperez on 5/21/18.
 * Property of Instacarro.com
 */
data class PlaceSearch (var isLoading:MutableLiveData<Boolean> = MutableLiveData(),
                        var isError:MutableLiveData<Boolean> = MutableLiveData(),
                        var places: MutableLiveData<PlaceSearchResponse> = MutableLiveData())