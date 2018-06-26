package com.eury.touristai

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.eury.touristai.repository.local.AppDb
import android.os.Build
import java.util.*
import android.os.LocaleList
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import android.support.multidex.MultiDex




/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */
class TouristAI : MultiDexApplication() {

    private val DB_NAME:String = "SSI_DB"

    init {
        instance = this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        MobileAds.initialize(this, getString(R.string.admob_app_id))
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