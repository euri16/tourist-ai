package com.eury.touristai.ui.main.adapters

import android.content.Context
import android.view.ViewGroup
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.ui.main.adapters.itemview.PlaceItemView

/**
 * Created by euryperez on 3/2/18.
 */
class PlacesAdapter(override var context: Context, items: MutableList<Place>?,
                    private val onClick: (place: Place?) -> Unit) :
        BaseRecyclerViewAdapter<Place, PlaceItemView>(context, items ?: mutableListOf()),
        PlaceItemView.OnPlaceSelectedListener {

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): PlaceItemView {
        return PlaceItemView(context, this)
    }

    override fun getItem(item: Place): Place {
        items?.let { list -> return list.single { (it.placeId == item.placeId)  } }
        return Place("")
    }

    override fun onBindViewHolder(holder: ViewHolderWrapper<PlaceItemView>, position: Int) {
        val item = items?.get(position)
        val itemView = holder.view
        itemView.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onPlaceSelected(place: Place?) {
        onClick(place)
    }
}