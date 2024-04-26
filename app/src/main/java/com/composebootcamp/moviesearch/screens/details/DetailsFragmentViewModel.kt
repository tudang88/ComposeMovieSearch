package com.composebootcamp.moviesearch.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.convertToMovieEntry
import com.composebootcamp.moviesearch.network.model.getAllGenres
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import com.example.moviessearch.utils.convertTimeToString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Movie Detail Fragment
 */
class DetailsFragmentViewModel(private val repository: MovieResourceInterface) : ViewModel() {
    private val movieDetail = repository.getMovieDetails()

    @OptIn(ExperimentalCoroutinesApi::class)
    val backdropImageUrl = movieDetail.mapLatest {
        if (it?.backdropPath != null) {
            it.backdropPath
        } else it?.posterPath
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val title =
        movieDetail.map { it?.title ?: "" }.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val releaseDate = movieDetail.map { it?.releaseDate ?: "" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val genres = movieDetail.map { it?.genres?.getAllGenres() ?: "" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val movieTime = movieDetail.map { convertTimeToString(it?.runtime) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val rating: StateFlow<Int> =
        movieDetail.map { (((it?.rating ?: 0.0)).toFloat() * 10).toInt() }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly, 0
        )
    val overview =
        movieDetail.map { it?.overview }.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    private val _favorite = MutableStateFlow<Boolean>(false)
    val favorite: StateFlow<Boolean> =
        _favorite.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    /**
     * retrieve Movie Detail from remote
     */
    fun getMovieDetailsInfo(movie: MovieEntry) {
        Timber.d("getMovieDetailsInfo($movie)")
        _favorite.value = movie.favoriteFlag
        viewModelScope.launch {
            repository.getMovieDetailsInfo(movie.id)
        }
    }

    /**
     * handling favorite change event
     */
    fun onFavoriteChanged(isCheck: Boolean) {
        Timber.d("onFavoriteChanged - $isCheck")
        viewModelScope.launch {
            movieDetail.collect() {
                val movie = it?.convertToMovieEntry()
                if (movie != null) {
                    repository.changeMovieStatusOnFavoriteDb(movie, isCheck)
                }
                _favorite.value = isCheck
            }

        }
    }
}