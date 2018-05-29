package com.eury.touristai.repository.remote.requests

import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
interface PlacesRequests {
    @GET("textsearch/json")
    fun getPlaceDetails(
            @Query("query") query: String,
            @Query("key") key: String,
            @Query("language") lang: String = "es"
    ): Call<PlaceSearchResponse>
}