package com.eury.touristai.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.eury.touristai.R
import com.eury.touristai.TouristAI
import com.eury.touristai.databinding.PlaceSearchFragmentBinding
import com.eury.touristai.repository.remote.models.PlaceSearchResponse
import com.eury.touristai.ui.main.viewmodels.PlaceSearchViewModel
import com.mvc.imagepicker.ImagePicker
import com.eury.touristai.ui.main.adapters.PlacesAdapter
import com.eury.touristai.utils.ExifUtil
import kotlinx.android.synthetic.main.place_search_fragment.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.uiThread

class PlaceSearchFragment : Fragment() {

    private lateinit var viewModel: PlaceSearchViewModel
    private lateinit var mAdapter: PlacesAdapter
    private lateinit var binding: PlaceSearchFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_search_fragment, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaceSearchViewModel::class.java)
        binding.viewModel = viewModel

        // setting up recyclerview
        mAdapter = PlacesAdapter(context!!, mutableListOf()) { place ->
            onPlaceSelected(place)
        }

        ivPickImage.setOnClickListener {
            ImagePicker.pickImage(this, "Select your image:")
        }

        ImagePicker.setMinQuality(600, 600)

        pulsator.start()

        /*rvPlaces.setup(context!!)
        rvPlaces.adapter = mAdapter*/

        viewModel.getPlaces().observe(this, Observer {response ->
            response?.let {
                val placeId = response.results?.get(0)?.placeId
                val placeName = response.results?.get(0)?.name
                val action = PlaceSearchFragmentDirections.placeDetailAction(placeId, placeName)
                findNavController().navigate(action)
            }
        })
    }

    private fun onPlaceSelected(place: PlaceSearchResponse.Result?) {
        val bundle = bundleOf("place" to place)
        NavHostFragment.findNavController(this).navigate(R.id.placeDetailAction, bundle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = ImagePicker.getImagePathFromResult(context, requestCode, resultCode, data)
        path?.let { compressPictureAndSubmit(it) }
    }

    private fun compressPictureAndSubmit(imagePath: String) {
        doAsync {
            val bitmap = ExifUtil.resizeBitmap(imagePath, 100)
            uiThread {
                bitmap.let {
                    viewModel.submitBitmapForAnalysis(bitmap)
                }
            }
        }
    }
}