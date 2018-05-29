package com.eury.touristai.repository.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Created by euryperez on 5/22/18.
 * Property of Instacarro.com
 */
data class WikiResponse(
        var title: String? = null,

        @SerializedName("displaytitle")
        var displayTitle: String? = null,

        var extract: String? = null,

        var extract_html: String? = null

)