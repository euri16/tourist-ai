package com.eury.touristai.repository.remote.requests

import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.remote.models.WikiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
interface WikiRequests {
    @GET("page/summary/{placeName}")
    fun getPlaceInfo(
            @Path("placeName", encoded = true) placeName: String
    ): Call<WikiResponse>
}