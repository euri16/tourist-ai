package com.eury.touristai.repository.remote.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by euryperez on 6/11/18.
 * Property of Instacarro.com
 */
class PlaceDetailsResponse constructor() : Serializable {

    val result: Result? = null

    data class OpenDetails(
            var time: String? = null,
            var day: String? = null
    ) : Serializable

    data class Periods(
            var open: OpenDetails? = null,
            var close: OpenDetails? = null
    ) : Serializable

    data class OpeningHours(
            @SerializedName("open_now")
            var openNow: Boolean? = null,
            var periods: List<Periods>? = null

    ) : Serializable

    data class Result (
        val international_phone_number: String? = null,

        @SerializedName("opening_hours")
        val openingHours: OpeningHours? = null,
        @SerializedName("utc_offset")
        val utcOffset:Int = 0
    ) : Serializable
}