package com.eury.touristai.repository.remote.requests

import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.remote.models.WikiQueryResponse
import com.eury.touristai.repository.remote.models.WikiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
interface WikiRequests {
    @GET("page/summary/{placeName}")
    fun getPlaceInfo(
            @Path("placeName", encoded = true) placeName: String
    ): Call<WikiResponse>

    @GET
    fun getQueryResults(
            @Url url:String = "https://en.wikipedia.org/w/api.php",
            @Query("action") action:String = "query",
            @Query("format") format:String = "json",
            @Query("list") list:String = "search",
            @Query("utf8") utf8:String = "1",
            @Query("srsearch") queryText:String
    ): Call<WikiQueryResponse>
}