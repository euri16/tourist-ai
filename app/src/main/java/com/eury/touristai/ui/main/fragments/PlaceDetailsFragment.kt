package com.eury.touristai.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eury.touristai.R
import com.eury.touristai.databinding.PlaceDetailsFragmentBinding
import com.eury.touristai.ui.main.viewmodels.PlaceDetailsViewModel
import com.eury.touristai.utils.Loggable.Companion.log

/**
 * Created by euryperez on 5/17/18.
 * Property of Instacarro.com
 */

class PlaceDetailsFragment : Fragment() {

    private lateinit var viewModel: PlaceDetailsViewModel
    private lateinit var binding: PlaceDetailsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_details_fragment, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaceDetailsViewModel::class.java)
        binding.viewModel = viewModel

        val placeId = PlaceDetailsFragmentArgs.fromBundle(arguments).placeId
        val placeName = PlaceDetailsFragmentArgs.fromBundle(arguments).placeName

        viewModel.getPlace(placeId).observe(this, Observer {
            log.d("--")
        })

        viewModel.fetchPlaceDetailInfo(placeId, placeName)
    }
}