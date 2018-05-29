package com.eury.touristai.repository.remote.requests

import com.eury.touristai.repository.remote.models.VisionLandmarkRequest
import com.eury.touristai.repository.remote.models.VisionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
interface VisionRequests {

    @POST("./images:annotate/")
    fun submitImageForAnalysis(
            @Body requests: VisionLandmarkRequest,
            @Query("key") key: String
    ): Call<VisionResponse>
}