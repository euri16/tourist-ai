package com.eury.touristai.repository.remote.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by euryperez on 6/14/18.
 * Property of Instacarro.com
 */

data class WikiQueryResponse(
        @SerializedName("query")
        @Expose
        val query: Query?
)

data class Query(
        @SerializedName("search")
        @Expose
        var search: List<Search>? = null
)

data class Search(
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("pageid")
        @Expose
        var pageid: Int? = null
)