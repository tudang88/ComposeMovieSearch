package com.composebootcamp.moviesearch.screens.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.composebootcamp.moviesearch.R
import com.composebootcamp.moviesearch.databinding.FragmentSearchBinding
import com.composebootcamp.moviesearch.utils.MoviesRecyclerViewAdapter
import com.composebootcamp.moviesearch.utils.OnMovieItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


/**
 * A fragment for showing search result
 */
class SearchFragment : Fragment() {
    private val _viewModel: SearchFragmentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )
        // binding viewModel to layout
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this
        // create adapter for Result list
        binding.searchResultList.adapter = MoviesRecyclerViewAdapter(onMovieClick, null)
        binding.searchButton.setOnClickListener {
            _viewModel.onSearchMovies()
            hideKeyboard()
        }
        return binding.root
    }

    /**
     * click listener when user click movie card
     */
    private val onMovieClick = OnMovieItemClickListener {
        Timber.d("Click item: ${it.title}")
        // transition to DetailsFragment
        findNavController().navigate(
            SearchFragmentDirections.showMovieDetailsFromSearch(
                it
            )
        )
    }

    /**
     * hide softKeyboard when it's not necessary
     */
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}