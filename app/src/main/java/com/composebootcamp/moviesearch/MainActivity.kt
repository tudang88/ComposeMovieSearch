package com.composebootcamp.moviesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.composebootcamp.moviesearch.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
//        navController.addOnDestinationChangedListener{
//            _, destination, _ ->
//            if (destination.id == R.id.detailsFragment) {
//
//            }
//
//        }
        // set up bottom nav view
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        // observer number of record in database to update favorite badge
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.favoriteCount.collect {
                    it.let {
                        Timber.d("Database count change to : $it")
                        updateFavoriteBadge(it)
                    }
                }
            }
        }

//        observe(this, Observer {
//            it?.let {
//                Timber.d("Database count change to : $it")
//                updateFavoriteBadge(it)
//            }
//        })
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