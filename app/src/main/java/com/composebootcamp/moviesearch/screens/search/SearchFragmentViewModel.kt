package com.composebootcamp.moviesearch.screens.search

import androidx.lifecycle.*
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
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
    val searchResult = repository.getSearchResult()
    val foundMovieCount = searchResult.map { it.size }

    /**
     * initialize viewModel
     */
    init {
        Timber.d("init ViewModel Live Data")
        keyword.value = ""
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