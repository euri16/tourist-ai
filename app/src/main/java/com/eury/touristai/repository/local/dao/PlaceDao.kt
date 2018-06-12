package com.eury.touristai.repository.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.eury.touristai.repository.local.entities.Place
import java.util.*

/**
 * Created by euryperez on 5/20/18.
 * Property of Instacarro.com
 */

@Dao
interface PlaceDao {

    @Insert(onConflict = REPLACE)
    fun save(vararg places: Place)

    @Update
    fun update(vararg places:Place)

    @Query("SELECT * from places WHERE place_id = :placeId")
    fun loadPlaceById(placeId:String) : LiveData<Place>

    @Query("SELECT * from places WHERE originalName = :placeName")
    fun loadPlaceByVisionName(placeName:String) : Place

    @Query("SELECT * from places WHERE place_id = :placeId")
    fun loadPlace(placeId:String) : Place

    @Query("SELECT * from places")
    fun loadAll() : LiveData<List<Place>>
}