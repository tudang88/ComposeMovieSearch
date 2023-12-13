package com.composebootcamp.moviesearch.screens.details

import androidx.lifecycle.*
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.convertToMovieEntry
import com.composebootcamp.moviesearch.network.model.getAllGenres
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import com.example.moviessearch.utils.convertTimeToString
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for Movie Detail Fragment
 */
class DetailsFragmentViewModel(private val repository: MovieResourceInterface) : ViewModel() {
    private val movieDetail = repository.getMovieDetails()
    val backdropImageUrl =
        movieDetail.map { if (it.backdropPath != null) {
            it.backdropPath
        } else it.posterPath }
    val title = movieDetail.map { it.title }
    val releaseDate = movieDetail.map { it.releaseDate }
    val genres = movieDetail.map { it.genres.getAllGenres() }
    val movieTime = movieDetail.map { convertTimeToString(it.runtime) }
    val rating = movieDetail.map { (it.rating * 10).toInt() }
    val overview = movieDetail.map { it.overview }
    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean>
        get() = _favorite


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
            val movie = movieDetail.value?.convertToMovieEntry()
            if (movie != null) {
                repository.changeMovieStatusOnFavoriteDb(movie, isCheck)
                _favorite.value = isCheck
            }
        }
    }
}