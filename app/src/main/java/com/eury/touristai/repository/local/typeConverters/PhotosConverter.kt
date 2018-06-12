package com.eury.touristai.repository.local.typeConverters

import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.util.StringUtil
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */
class PhotosConverter {

    @TypeConverter
    fun toPhotoReferences(json: String?): List<String> {
        if(TextUtils.isEmpty(json)){
            return listOf()
        }

        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun fromPhotoReferences(photos: List<String>?): String {
        photos?.let {
            return Gson().toJson(photos)
        }
        return ""
    }
}