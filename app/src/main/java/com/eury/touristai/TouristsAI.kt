package com.eury.touristai

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.eury.touristai.repository.local.AppDb
import android.os.Build
import java.util.*
import android.os.LocaleList
import net.danlew.android.joda.JodaTimeAndroid


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
        JodaTimeAndroid.init(this)
        TouristAI.database = Room.databaseBuilder(this, AppDb::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

        changeLocale(Locale("en", "US"))
    }

    fun changeLocale(locale: Locale) {
        val conf = resources.configuration
        conf.locale = locale
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(conf.locale)
        }
        resources.updateConfiguration(conf, resources.displayMetrics)
    }

    companion object {
        var database:AppDb? = null
        var instance:TouristAI? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}