package com.composebootcamp.moviesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.composebootcamp.moviesearch.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val _viewModel: MainActivityViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        // find nav controller
        navController = findNavController(R.id.nav_host_fragment)
        // set up bottom nav view
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        // observer number of record in database to update favorite badge
        _viewModel.favoriteCount.observe(this, Observer {
            it?.let {
                Timber.d("Database count change to : $it")
                updateFavoriteBadge(it)
            }
        })
    }

    /**
     * update favorite badge
     */
    private fun updateFavoriteBadge(count: Int) {
        val navBar = binding.bottomNav
        if (count <= 0) {
            navBar.removeBadge(R.id.favoriteFragment)
        } else {
            val badge = navBar.getOrCreateBadge(R.id.favoriteFragment)
            badge.number = count
        }
    }
}