package com.eury.touristai.utils

import com.eury.touristai.R
import com.eury.touristai.TouristAI

/**
 * Created by euryperez on 5/29/18.
 * Property of Instacarro.com
 */

object Credentials {
    var placesApiKey = TouristAI.applicationContext().getString(R.string.places_api_key)
    var visionApiKey = TouristAI.applicationContext().getString(R.string.vision_api_key)
}