package com.eury.touristai.ui.main.adapters.itemview

import android.content.Context
import android.view.View
import com.eury.touristai.R
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.remote.models.VisionResponse
import com.eury.touristai.utils.Credentials
import com.eury.touristai.utils.Loggable
import com.eury.touristai.utils.loadImage
import kotlinx.android.synthetic.main.itv_place.view.*
import kotlinx.android.synthetic.main.place_search_fragment.view.*


/**
 * Created by euryperez on 3/2/18.
 */

class PlaceItemView(context: Context) : BaseItemView<Place?>
(context, R.layout.itv_place), Loggable, View.OnClickListener {

    private var mListener: OnPlaceSelectedListener? = null

    constructor(context: Context, listener: OnPlaceSelectedListener) : this(context) {
        mListener = listener
    }

    init {
        setOnClickListener(this)
    }

    override fun bind(entity: Place?) {
        super.bind(entity)
        tvPlaceName.text = entity?.name
        entity?.photoReferences?.get(0)?.let {
            val url = context.getString(R.string.google_photo_reference, it, 1024, 1024, Credentials.placesApiKey)
            ivPlacePicture.loadImage(url)
        }
    }

    override fun onClick(p0: View?) {
        mListener?.onPlaceSelected(item)
    }

    interface OnPlaceSelectedListener {
        fun onPlaceSelected(place: Place?)
    }
}