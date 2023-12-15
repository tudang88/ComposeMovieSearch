package com.composebootcamp.moviesearch.screens.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.composebootcamp.moviesearch.R
import com.composebootcamp.moviesearch.databinding.FragmentDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val _viewModel: DetailsFragmentViewModel by viewModel()
    private val args: DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentDetailsBinding>(
            inflater,
            R.layout.fragment_details,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel
        binding.closeDetailsButton.setOnClickListener {
            exitFromDetails()
        }
        return binding.root
    }

    /**
     * exit from Details
     */
    private fun exitFromDetails() {
        findNavController().popBackStack()
    }

    /**
     * Hide bottom bar when showing detail fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.getMovieDetailsInfo(args.movieEntry)
    }

    override fun onResume() {
        super.onResume()
        val naviBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        naviBar?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        val naviBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        naviBar?.visibility = View.VISIBLE
    }
}