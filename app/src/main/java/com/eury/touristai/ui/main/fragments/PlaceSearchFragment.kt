package com.eury.touristai.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eury.touristai.R
import com.eury.touristai.databinding.PlaceSearchFragmentBinding
import com.eury.touristai.ui.main.viewmodels.PlaceSearchViewModel
import com.mvc.imagepicker.ImagePicker
import com.eury.touristai.ui.main.fragments.dialogs.TextSearchDialogFragment
import com.eury.touristai.utils.ExifUtil
import com.eury.touristai.utils.Loggable.Companion.log
import com.eury.touristai.utils.arePermissionsGranted
import com.eury.touristai.utils.getAppCompatActivity
import com.eury.touristai.utils.showDialog
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.place_search_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PlaceSearchFragment : Fragment() {

    private lateinit var viewModel: PlaceSearchViewModel
    private lateinit var binding: PlaceSearchFragmentBinding

    private var mTextSearchDialog: TextSearchDialogFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_search_fragment, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        initializeAd()
        initializeAnimationAndPicker()

        ivPickImage.setOnClickListener {
            ImagePicker.pickImage(this, "Select your image:")
        }

        tvRecentSearches.setOnClickListener {
            findNavController().navigate(R.id.searchHistoryAction)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(PlaceSearchViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.model.isError.observe(this, Observer { isError ->
            log.d("observing isError: ${viewModel.isTextSearchDialogShowing}")
            if (isError == true && !viewModel.isTextSearchDialogShowing) {
                viewModel.isTextSearchDialogShowing = true
                mTextSearchDialog = TextSearchDialogFragment.newInstance()
                showDialog(mTextSearchDialog!!, true, TEXT_SEARCH_DIALOG_TAG)
            }
        })

        viewModel.getPlaces().observe(this, Observer { response ->
            response?.let {
                val placeId = response.results?.get(0)?.placeId
                val placeName = response.results?.get(0)?.name
                val action = PlaceSearchFragmentDirections.placeDetailAction(placeId, placeName)
                findNavController().navigate(action)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = ImagePicker.getImagePathFromResult(context, requestCode, resultCode, data)
        path?.let { compressPictureAndSubmit(it) }
    }

    private fun initializeAd() {
        val adRequest = AdRequest.Builder()
                .addTestDevice("3C4B0849D78F920065BC35369C976A74")
                .build()
        adView.loadAd(adRequest)
    }

    private fun checkPermissions() {
        getAppCompatActivity()?.let { activity ->
            activity.supportActionBar?.hide()
            if (!activity.arePermissionsGranted(PlaceSearchViewModel.REQUIRED_PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity,
                        PlaceSearchViewModel.REQUIRED_PERMISSIONS,
                        PlaceSearchViewModel.PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun initializeAnimationAndPicker() {
        ImagePicker.setMinQuality(600, 600)
        pulsator.start()
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

    companion object {
        private const val TEXT_SEARCH_DIALOG_TAG = "0"
    }
}