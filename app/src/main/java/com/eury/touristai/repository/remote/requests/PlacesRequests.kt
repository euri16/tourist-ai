package com.eury.touristai.repository.remote.requests

import com.eury.touristai.repository.remote.models.PlaceDetailsResponse
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.utils.Credentials
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
interface PlacesRequests {
    @GET("textsearch/json")
    fun getPlace(
            @Query("query") query: String,
            @Query("key") key:String = Credentials.placesApiKey,
            @Query("language") lang: String = "en"
    ): Call<PlaceSearchResponse>

    @GET("details/json")
    fun getPlaceDetails(
            @Query("placeid") placeId: String,
            @Query("key") key: String = Credentials.placesApiKey,
            @Query("language") lang: String = "en"
    ): Call<PlaceDetailsResponse>
}