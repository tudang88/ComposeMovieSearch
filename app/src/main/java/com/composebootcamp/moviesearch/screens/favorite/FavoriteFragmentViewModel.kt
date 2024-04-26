package com.composebootcamp.moviesearch.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel of FavoriteFragment
 */
class FavoriteFragmentViewModel(private val repository: MovieResourceInterface) : ViewModel() {
    val allFavoriteData = repository.getAllFavoriteMovies()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val savedMovieCount =
        allFavoriteData.map { it.size }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    /**
     * handling favorite change event
     */
    fun onFavoriteChanged(movie: MovieEntry, isCheck: Boolean) {
        Timber.d("onFavoriteChanged - $isCheck")
        viewModelScope.launch {
            repository.changeMovieStatusOnFavoriteDb(movie, isCheck)
        }
    }
}