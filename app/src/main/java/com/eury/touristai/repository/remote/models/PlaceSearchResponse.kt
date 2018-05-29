package com.eury.touristai.repository.remote.models

import com.eury.touristai.repository.PlacesRepository
import com.eury.touristai.repository.local.entities.Place
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlaceSearchResponse constructor() : Serializable {

    var results: List<Result>? = null

    var status: String? = null


    constructor(placeName: String, existingPlace:Place) : this() {
        val result = PlaceSearchResponse.Result(placeId = existingPlace.placeId,
                                                placeName = placeName)
        results = listOf(result)
    }

    data class Geometry (var location: Location? = null)  : Serializable

    data class Location (var lat: Double? = null, var lng: Double? = null) : Serializable

    data class OpeningHours (@SerializedName("open_now") var openNow: Boolean? = null) : Serializable

    class Result constructor() : Serializable {
        var geometry: Geometry? = null
        var icon: String? = null
        var id: String? = null
        var name: String? = null
        @SerializedName("formatted_address")
        var formattedAddress: String? = null
        @SerializedName("opening_hours")
        @Expose
        var openingHours: OpeningHours? = null
        var photos: List<Photo>? = null
        @SerializedName("place_id")
        @Expose
        var placeId: String? = null
        var rating: Float? = null
        var scope: String? = null
        var reference: String? = null
        var types: List<String>? = null
        var vicinity: String? = null

        constructor(placeId:String, placeName:String) : this() {
            this.placeId = placeId
            this.name = placeName
        }

        fun buildPlace(placeDescription: PlacesRepository.PlaceDescription? = null) : Place {

            val place = Place(placeId = this.placeId!!)
            //place.openingHours = placeResult.openingHours
            place.photoReferences = this.photos?.map { it.photoReference!! }
            place.location = this.geometry?.location
            place.address = this.formattedAddress
            place.rating = this.rating
            place.originalName = placeDescription?.name
            place.wikiPageTitle = placeDescription?.wikiPageTitle

            return place
        }
    }

    class Photo : Serializable {
        var height: Int? = null

        @SerializedName("photo_reference")
        @Expose
        var photoReference: String? = null

        var width: Int? = null
    }
}