package com.eury.touristai

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.eury.touristai.repository.local.AppDb

/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
class TouristAI : Application() {

    private val DB_NAME:String = "SSI_DB"

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        TouristAI.database = Room.databaseBuilder(this, AppDb::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    companion object {
        var database:AppDb? = null
        var instance:TouristAI? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}