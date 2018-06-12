package com.eury.touristai.ui.main.bindingAdapters

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.eury.touristai.utils.fadeIn
import com.eury.touristai.utils.fadeOut
import com.bumptech.glide.Glide




/**
 * Created by euryperez on 5/15/18.
 * Property of Instacarro.com
 */
object BindingAdapters {


    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view:View, visible: Boolean) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible) {
                view.fadeIn()
            } else {
                view.fadeOut()
            }
        }
    }

    @BindingAdapter("android:visibility")
    @JvmStatic
    fun setViewVisibility(view:View, visible: Boolean) {
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun setImageUrl(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }

}