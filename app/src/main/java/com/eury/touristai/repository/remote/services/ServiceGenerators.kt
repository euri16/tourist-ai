package com.eury.touristai.repository.remote.services

import com.eury.touristai.BuildConfig

object PlacesServiceGenerator : BaseServiceGenerator(BuildConfig.PLACES_BASE_URL)
object GoogleCloudServiceGenerator : BaseServiceGenerator(BuildConfig.GOOGLE_CLOUD_BASE_URL)
object WikipediaServiceGenerator : BaseServiceGenerator(BuildConfig.WIKI_BASE_URL)