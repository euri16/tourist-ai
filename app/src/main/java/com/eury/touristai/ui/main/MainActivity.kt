package com.eury.touristai.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.eury.touristai.R
import com.eury.touristai.ui.main.fragments.dialogs.TextSearchDialogFragment
import com.eury.touristai.ui.main.viewmodels.PlaceSearchViewModel
import com.eury.touristai.utils.Loggable.Companion.log

class MainActivity : AppCompatActivity(), TextSearchDialogFragment.OnTextSearchListener {
    private var viewModel: PlaceSearchViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel = ViewModelProviders.of(this).get(PlaceSearchViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        setupActionBar(navController)


        navController.addOnNavigatedListener { _, destination ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    private fun setupActionBar(navController: NavController) {

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(null,
                    Navigation.findNavController(this, R.id.my_nav_host_fragment))


    override fun onTextSubmitted(text: String) {
        log.d(text)
        viewModel?.submitPlaceName(placeName = text)
    }

}
