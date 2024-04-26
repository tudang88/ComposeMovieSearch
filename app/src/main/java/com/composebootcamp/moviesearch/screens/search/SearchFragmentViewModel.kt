package com.composebootcamp.moviesearch.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchFragmentViewModel(private val repository: MovieResourceInterface) : ViewModel() {
    /**
     * user input keyword will be captured by this live data
     */
    var keyword = MutableLiveData<String>()


    /**
     * live data of search result
     */
    val searchResult =
        repository.getSearchResult().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val foundMovieCount =
        searchResult.map { it.size }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    /**
     * initialize viewModel
     */
    init {
        Timber.d("init ViewModel Live Data")

    }

    /**
     * handling event when user click on search button
     */
    fun onSearchMovies() {
        viewModelScope.launch {
            Timber.d("onSearchMovies(${keyword.value})")
            repository.getMoviesByKeyword(keyword.value ?: "")
        }
    }

}