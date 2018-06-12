package com.eury.touristai.repository.local.typeConverters

import android.arch.persistence.room.TypeConverter
import com.eury.touristai.repository.remote.models.PlaceDetailsResponse
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.google.gson.Gson

/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */
class OpeningHoursConverter {

    @TypeConverter
    fun toOpeningHours(json: String?): PlaceDetailsResponse.OpeningHours? {
        if(json == null) return null
        return Gson().fromJson(json, PlaceDetailsResponse.OpeningHours::class.java)
    }

    @TypeConverter
    fun fromOpeningHours(openingHours: PlaceDetailsResponse.OpeningHours?): String? {
        if(openingHours == null) return null
        return Gson().toJson(openingHours)
    }
}