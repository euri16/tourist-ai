package com.eury.touristai.ui.main.bindingAdapters

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.view.View
import com.eury.touristai.utils.fadeIn
import com.eury.touristai.utils.fadeOut


/**
 * Created by euryperez on 5/15/18.
 * Property of Instacarro.com
 */
object BindingAdapters {


    @BindingAdapter("fadeVisible")
    @JvmStatic
    fun View.setFadeVisible(visible: MutableLiveData<Boolean>) {
        if (this.tag == null) {
            this.tag = true
            this.visibility = if (visible.value == true) View.VISIBLE else View.GONE
        } else {
            this.animate().cancel()
            if (visible.value == true) {
                this.fadeIn()
            } else {
                this.fadeOut()
            }
        }
    }

}