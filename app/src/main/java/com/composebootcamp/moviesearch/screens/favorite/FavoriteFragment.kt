package com.composebootcamp.moviesearch.screens.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.composebootcamp.moviesearch.R
import com.composebootcamp.moviesearch.databinding.FragmentFavoriteBinding
import com.composebootcamp.moviesearch.utils.MoviesRecyclerViewAdapter
import com.composebootcamp.moviesearch.utils.OnFavoriteChangedListener
import com.composebootcamp.moviesearch.utils.OnMovieItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavoriteFragment : Fragment() {
    private val _viewModel: FavoriteFragmentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentFavoriteBinding>(
            inflater,
            R.layout.fragment_favorite,
            container,
            false
        )
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this
        // create adapter for Result list
        binding.favoriteList.adapter = MoviesRecyclerViewAdapter(onMovieClick, onFavoriteChanged)
        return binding.root
    }

    /**
     * resume bottom bar
     */
    override fun onResume() {
        super.onResume()
        activity?.bottom_nav?.visibility = View.VISIBLE
    }

    /**
     * click listener when user click movie card
     */
    private val onMovieClick = OnMovieItemClickListener {
        Timber.d("Click item: ${it.title}")
        // transition to DetailsFragment
        findNavController().navigate(
            FavoriteFragmentDirections.showMovieDetailFromFavorite(
                it
            )
        )
    }

    /**
     * favorite status change listener
     */
    private val onFavoriteChanged = OnFavoriteChangedListener { movie, checked ->
        Timber.d("Movie: ${movie.title} Favorite -> $checked")
        _viewModel.onFavoriteChanged(movie, checked)
    }
}