package com.eury.touristai.ui.main.adapters.itemview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.eury.touristai.utils.Loggable

/**
 * Created by euryperez on 3/2/18.
 */

abstract class BaseItemView<T> @JvmOverloads
    constructor(context: Context, var layout:Int, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context), Loggable {

    protected var item: T? = null

    init {
        val view = LayoutInflater.from(context).inflate(this.layout, this, false)
        this.addView(view)

        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams = params
    }

    internal open fun bind(entity: T) {
        item = entity
    }
}
