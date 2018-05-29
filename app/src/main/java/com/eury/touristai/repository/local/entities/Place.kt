package com.eury.touristai.repository.local.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.eury.touristai.repository.local.typeConverters.OpeningHoursConverter
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import android.arch.persistence.room.TypeConverters
import com.eury.touristai.repository.local.typeConverters.LocationConverter
import com.eury.touristai.repository.local.typeConverters.PhotosConverter

/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */

@Entity(tableName = "places")
data class Place (
    @PrimaryKey
    @ColumnInfo(name="place_id")
    var placeId:String,

    var name:String? = null,

    var originalName:String? = null,

    var wikiPageTitle:String? = null,

    @TypeConverters(OpeningHoursConverter::class)
    var openingHours: PlaceSearchResponse.OpeningHours? = null,

    @TypeConverters(PhotosConverter::class)
    var photoReferences: List<String>? = null,

    @TypeConverters(LocationConverter::class)
    var location:PlaceSearchResponse.Location? = null,

    var address:String? = null,

    var phoneNumber:String? = null,

    var rating:Float? = 0f,

    var summaryInfo:String? = null
)