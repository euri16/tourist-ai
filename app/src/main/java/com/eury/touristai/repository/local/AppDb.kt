package com.eury.touristai.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.eury.touristai.repository.local.dao.PlaceDao
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.local.typeConverters.LocationConverter
import com.eury.touristai.repository.local.typeConverters.OpeningHoursConverter
import com.eury.touristai.repository.local.typeConverters.PhotosConverter

/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */
@TypeConverters(LocationConverter::class, OpeningHoursConverter::class, PhotosConverter::class)
@Database(entities = [Place::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun placeDao() : PlaceDao
}