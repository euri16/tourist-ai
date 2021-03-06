package com.eury.touristai.utils

import android.Manifest
import android.animation.Animator
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
import java.text.Normalizer
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import com.eury.touristai.R


/**
 * Created by euryperez on 5/10/18.
 * Property of Instacarro.com
 */

fun Fragment.getAppCompatActivity() : AppCompatActivity? {
    return (activity as? AppCompatActivity)
}


fun AppCompatActivity.arePermissionsGranted(permissions: Array<String>): Boolean {
    return permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
}

fun Context.openPlaceInMap(placeId:String) {
    val gmmIntentUri = Uri.parse(getString(R.string.maps_query_intent, placeId))
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
        startActivity(mapIntent)
    }
}

fun Context.openPhoneDialer(phoneNumber:String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    startActivity(intent)
}

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

fun String.capsWord() : String {
    return this.split(' ').joinToString(" ") { it.capitalize() }
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
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
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
        dialogFragment.isCancelable = cancelable
        dialogFragment.show(childFragmentManager, tag)
    } catch (ex: IllegalStateException) {
        log.d(ex.localizedMessage)
    }
}