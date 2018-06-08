package com.eury.touristai.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.eury.touristai.repository.remote.CustomCallback
import com.eury.touristai.utils.Loggable.Companion.log
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import android.view.animation.AccelerateInterpolator
import java.text.Normalizer


/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this).load(url).into(this)
}

fun RecyclerView.setup(context: Context) {
    this.setHasFixedSize(true)
    this.itemAnimator = DefaultItemAnimator()
    this.layoutManager = LinearLayoutManager(context)
}

fun String.stripAccents(): String {
    var string = Normalizer.normalize(this, Normalizer.Form.NFD)
    string = Regex("\\p{InCombiningDiacriticalMarks}+").replace(string, "")
    return string
}

fun <T> Call<T>.perform(onCallCompleted: (response: T?, isError: Boolean, t: Throwable?) -> Unit) {
    this.enqueue(object : CustomCallback<T>() {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!response.isSuccessful) {
                onCallCompleted(null, true, null)
                return
            }
            onCallCompleted(response.body(), false, null)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            super.onFailure(call, t)
            onCallCompleted(null, true, t)
            log.e(t.message ?: "")
        }
    })
}

fun View.fadeIn() {
    this.alpha = 0f
    this.animate().alpha(1f).setDuration(1000).setInterpolator(AccelerateInterpolator()).start()
}

fun View.fadeOut() {
    this.alpha = 1f
    this.animate().alpha(0f).setDuration(1000).setInterpolator(AccelerateInterpolator()).start()
}

fun FragmentActivity.showDialog(dialogFragment: DialogFragment, cancelable: Boolean, tag: String) {
    try {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialogFragment.isCancelable = cancelable
        fragmentTransaction.add(dialogFragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    } catch (ex: IllegalStateException) {
        log.d(ex.localizedMessage)
    }
}

fun Fragment.showDialog(dialogFragment: DialogFragment, cancelable: Boolean, tag: String) {
    try {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        dialogFragment.isCancelable = cancelable
        fragmentTransaction.add(dialogFragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    } catch (ex: IllegalStateException) {
        log.d(ex.localizedMessage)
    }
}