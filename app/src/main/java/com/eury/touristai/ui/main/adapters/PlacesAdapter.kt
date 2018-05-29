package com.eury.touristai.ui.main.adapters

import android.content.Context
import android.view.ViewGroup
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.ui.main.adapters.itemview.PlaceItemView

/**
 * Created by euryperez on 3/2/18.
 */
class PlacesAdapter(override var context: Context, items: MutableList<PlaceSearchResponse.Result?>,
                    private val onClick: (place: PlaceSearchResponse.Result?) -> Unit) :
        BaseRecyclerViewAdapter<PlaceSearchResponse.Result?, PlaceItemView>(context, items),
        PlaceItemView.OnPlaceSelectedListener {

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): PlaceItemView {
        return PlaceItemView(context, this)
    }

    override fun getItem(item: PlaceSearchResponse.Result?): PlaceSearchResponse.Result? {
        items?.let { list -> return list.single { it?.id?.equals(item?.id) ?: false } }
        return null
    }

    override fun onBindViewHolder(holder: ViewHolderWrapper<PlaceItemView>, position: Int) {
        val item = items?.get(position)
        val itemView = holder.view
        itemView.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onPlaceSelected(place: PlaceSearchResponse.Result?) {
        onClick(place)
    }
}