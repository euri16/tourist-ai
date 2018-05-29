package com.eury.touristai.repository.remote

import com.eury.touristai.utils.Loggable.Companion.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class CustomCallback<T> : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        log.e(t.localizedMessage)
    }
}
