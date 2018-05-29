package com.eury.touristai.repository.local.typeConverters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */
class PhotosConverter {

    @TypeConverter
    fun toPhotoReferences(json: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun fromPhotoReferences(photos: List<String>): String {
        return Gson().toJson(photos)
    }
}