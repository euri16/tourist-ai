package com.eury.touristai.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.State
import androidx.work.WorkManager
import com.eury.touristai.R
import com.eury.touristai.databinding.PlaceDetailsFragmentBinding
import com.eury.touristai.ui.main.viewmodels.PlaceDetailsViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.place_details_fragment.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import java.util.*


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
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        viewModel = ViewModelProviders.of(this).get(PlaceDetailsViewModel::class.java)
        binding.viewModel = viewModel

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val shimmerLayouts = arrayOf(shlTitle, shlSummary)
        shimmerLayouts.forEach { it.startShimmerAnimation() }

        val placeId = PlaceDetailsFragmentArgs.fromBundle(arguments).placeId

        viewModel.getPlace(placeId)?.observe(this, Observer { place ->
            val lat = place?.location?.lat
            val lon = place?.location?.lng

            if(lat is Double && lon is Double) {
                updateMapLocation(lat, lon)
            }

            tvOpenMaps.setOnClickListener {
                val gmmIntentUri = Uri.parse(getString(R.string.maps_query_intent, place?.placeId))
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                mapIntent.resolveActivity(context?.packageManager)?.let {
                    context?.startActivity(mapIntent)
                }
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
