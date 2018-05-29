package com.eury.touristai.repository.local.typeConverters

import android.arch.persistence.room.TypeConverter
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.google.gson.Gson

/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */
class LocationConverter {

    @TypeConverter
    fun toLocation(json: String): PlaceSearchResponse.Location {
        return Gson().fromJson(json, PlaceSearchResponse.Location::class.java)
    }

    @TypeConverter
    fun fromLocation(location: PlaceSearchResponse.Location): String {
        return Gson().toJson(location)
    }
}