package com.eury.touristai.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eury.touristai.R
import com.eury.touristai.repository.local.entities.Place
import com.eury.touristai.ui.main.adapters.PlacesAdapter

import com.eury.touristai.ui.main.viewmodels.SearchHistoryListViewModel
import com.eury.touristai.utils.setup
import kotlinx.android.synthetic.main.search_history_list_fragment.*

class SearchHistoryListFragment : Fragment() {

    private lateinit var viewModel: SearchHistoryListViewModel

    private lateinit var mAdapter: PlacesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_history_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchHistoryListViewModel::class.java)

        (activity as? AppCompatActivity)?.supportActionBar?.show()

        // setting up recyclerview
        mAdapter = PlacesAdapter(context!!, mutableListOf()) { place ->
            onPlaceSelected(place)
        }
        rvItems.setup(context!!)
        rvItems.adapter = mAdapter

        viewModel.getPlaces()?.observe(this, Observer { places ->
            places?.let {
                mAdapter.addData(places, null)
            }
        })
    }

    private fun onPlaceSelected(place: Place?) {
        val placeId = place?.placeId
        val placeName = place?.name
        val action = PlaceSearchFragmentDirections.placeDetailAction(placeId, placeName)
        findNavController().navigate(action)
    }
}

