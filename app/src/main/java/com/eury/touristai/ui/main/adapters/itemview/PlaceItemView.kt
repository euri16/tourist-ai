package com.eury.touristai.ui.main.adapters.itemview

import android.content.Context
import android.view.View
import com.eury.touristai.R
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.repository.remote.models.VisionResponse
import com.eury.touristai.utils.Loggable
import com.eury.touristai.utils.loadImage
import kotlinx.android.synthetic.main.itv_place.view.*
import kotlinx.android.synthetic.main.place_search_fragment.view.*


/**
 * Created by euryperez on 3/2/18.
 */

class PlaceItemView(context: Context) : BaseItemView<PlaceSearchResponse.Result?>
(context, R.layout.itv_place), Loggable, View.OnClickListener {

    private var mListener: OnPlaceSelectedListener? = null

    constructor(context: Context, listener: OnPlaceSelectedListener) : this(context) {
        mListener = listener
    }

    init {
        setOnClickListener(this)
    }

    override fun bind(entity: PlaceSearchResponse.Result?) {
        super.bind(entity)
        tvPlaceName.text = entity?.name
        entity?.photos?.get(0)?.photoReference?.let {
            val url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$it&key=AIzaSyATG__L98EAthNwQ3fK8zDDrlk4-bWU-wE"
            ivPlacePicture.loadImage(url)
        }
    }

    override fun onClick(p0: View?) {
        mListener?.onPlaceSelected(item)
    }

    interface OnPlaceSelectedListener {
        fun onPlaceSelected(place: PlaceSearchResponse.Result?)
    }
}