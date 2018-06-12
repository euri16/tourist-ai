package com.eury.touristai.repository.local.entities

import android.arch.persistence.room.*
import com.eury.touristai.repository.local.typeConverters.OpeningHoursConverter
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.local.typeConverters.LocationConverter
import com.eury.touristai.repository.local.typeConverters.PhotosConverter
import com.eury.touristai.repository.remote.models.PlaceDetailsResponse

/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */

@Entity(tableName = "places")
class Place constructor(@ColumnInfo(name = "place_id") @PrimaryKey var placeId:String) {
    var name: String? = null

    var originalName: String? = null

    var wikiPageTitle: String? = null

    var webEntityTitle: String? = null

    @TypeConverters(OpeningHoursConverter::class)
    var openingHours: PlaceDetailsResponse.OpeningHours? = null

    var utcOffset: Int = 0

    @TypeConverters(PhotosConverter::class)
    var photoReferences: List<String>? = null

    @TypeConverters(LocationConverter::class)
    var location: PlaceSearchResponse.Location? = null

    var address: String? = null

    var phoneNumber: String? = null

    var rating: Float? = 0f

    var summaryInfo: String? = null
}