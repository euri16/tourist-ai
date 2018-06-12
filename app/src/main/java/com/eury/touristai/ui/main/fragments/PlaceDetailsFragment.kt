package com.eury.touristai.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.State
import androidx.work.WorkManager
import androidx.work.Worker
import com.eury.touristai.R
import com.eury.touristai.databinding.PlaceDetailsFragmentBinding
import com.eury.touristai.ui.main.viewmodels.PlaceDetailsViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.place_details_fragment.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng

/**
 * Created by euryperez on 5/17/18.
 * Property of Instacarro.com
 */

class PlaceDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: PlaceDetailsViewModel
    private lateinit var binding: PlaceDetailsFragmentBinding
    private var googleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_details_fragment, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaceDetailsViewModel::class.java)
        binding.viewModel = viewModel

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val shimmerLayouts = arrayOf(shlTitle, shlSummary)
        shimmerLayouts.forEach { it.startShimmerAnimation() }

        val placeId = PlaceDetailsFragmentArgs.fromBundle(arguments).placeId

        WorkManager.getInstance().getStatusesByTag(PlaceDetailsViewModel.FETCH_WIKI_DATA_WITH_NAME)
                .observe(this, Observer { workStatuses ->
                    val workStatus = workStatuses?.getOrNull(0)
                    if(workStatus?.state?.equals(State.FAILED) == true) {
                        viewModel.fetchWikiDetailInfo(placeId, workTag = PlaceDetailsViewModel.FETCH_WIKI_DATA_WITH_WIKI_TITLE)
                    }
                })

        WorkManager.getInstance().getStatusesByTag(PlaceDetailsViewModel.FETCH_WIKI_DATA_WITH_WIKI_TITLE)
                .observe(this, Observer { workStatuses ->
                    val workStatus = workStatuses?.getOrNull(0)
                    if(workStatus?.state?.equals(State.FAILED) == true) {
                        viewModel.fetchWikiDetailInfo(placeId, workTag = PlaceDetailsViewModel.FETCH_WIKI_DATA_WITH_WEB_ENTITY)
                    }
                })

        viewModel.getPlace(placeId)?.observe(this, Observer { place ->
            val lat = place?.location?.lat
            val lon = place?.location?.lng

            if(lat is Double && lon is Double) {
                updateMapLocation(lat, lon)
            }
        })

        viewModel.fetchWikiDetailInfo(placeId)
    }

    private fun updateMapLocation(lat:Double, lon:Double, title:String = "") {
        val coordinates = LatLng(lat, lon)
        googleMap?.addMarker(MarkerOptions().position(coordinates).title(title))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM), MAP_UPDATE_DURATION, null)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.googleMap = map
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val MAP_ZOOM = 15f
        private const val MAP_UPDATE_DURATION = 2000
    }
}
