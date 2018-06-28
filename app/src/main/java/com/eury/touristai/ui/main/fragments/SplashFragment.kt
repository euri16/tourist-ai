package com.eury.touristai.ui.main.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eury.touristai.R
import kotlinx.android.synthetic.main.fragment_splash.*
import androidx.navigation.fragment.findNavController
import com.eury.touristai.utils.getAppCompatActivity

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getAppCompatActivity()?.supportActionBar?.hide()

        particleView.startAnim()
        particleView.setOnParticleAnimListener {
            Handler().postDelayed({
                findNavController().navigate(R.id.mainFragment)
            }, 400)
        }
    }
}
