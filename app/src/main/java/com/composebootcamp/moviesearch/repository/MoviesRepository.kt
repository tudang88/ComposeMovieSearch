package com.composebootcamp.moviesearch.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.composebootcamp.moviesearch.database.FavoriteMovieDatabase
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.TmdbApi
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import com.composebootcamp.moviesearch.network.model.PosterSize
import com.composebootcamp.moviesearch.utils.EspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * The Repository will cover the data layer from upper layer (ViewModel and View)
 */
class MoviesRepository(
    private val localDb: FavoriteMovieDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieResourceInterface {
    private var baseImageUrl: String = ""


    /**
     * confirm an item already in database or not yet
     */
    override suspend fun isExistEntry(id: Int) = EspressoIdlingResource.wrapEspressoIdlingResource {
        withContext(ioDispatcher) { localDb.favoriteDbDao.isMovieAlreadyFavorite(id) }
    }

    /**
     * live data for search result
     * the result could be null in case no movie found
     */
    private val _searchResult = MutableLiveData<List<MovieEntry>>()
    override fun getSearchResult() = _searchResult

    /**
     * live data maintain number of database record
     * this number will be use to update favorite badge
     */
    private val _allFavoriteMovies =
        EspressoIdlingResource.wrapEspressoIdlingResource { localDb.favoriteDbDao.getAllFavoriteMovies() }

    override fun getAllFavoriteMovies() = _allFavoriteMovies
    override fun getNumOfDbRecord() = _allFavoriteMovies.map { it.size }

    private val _movieDetails = MutableLiveData<MovieDetailsResponse>()
    override fun getMovieDetails() = _movieDetails

    // get the baseImage Url for later use
    init {
        GlobalScope.launch {
            withContext(ioDispatcher) {
                try {
                    val response = TmdbApi.retrofitService.getConfiguration()
                    baseImageUrl = response.images.getDefaultImageUrl(PosterSize.W500)
                } catch (e: Exception) {
                    Timber.e("Failure on getting base image url ${e.localizedMessage}")
                }
            }
        }
    }

    /**
     * get movies by searching keyword
     */
    override suspend fun getMoviesByKeyword(keyword: String) {
        EspressoIdlingResource.wrapEspressoIdlingResource {
            val result = mutableListOf<MovieEntry>()
            Timber.d("getMoviesByKeyword($keyword)")
            try {
                val response = TmdbApi.retrofitService.searchMoviesByName(keyword)
                result.addAll(response.movieList.map {
                    var poster = ""
                    if (it.posterPath != null) {
                        poster = baseImageUrl + it.posterPath
                    }
                    MovieEntry(
                        it.id, poster,
                        it.title,
                        it.releaseDate,
                        it.overview
                    )
                })
            } catch (e: Exception) {
                Timber.e("Failure on search movies ${e.localizedMessage}")
            }

            // update UI
            withContext(Dispatchers.Main) {
                _searchResult.postValue(result)
            }
        }
    }

    /**
     * add movie to favorite
     */
    override suspend fun changeMovieStatusOnFavoriteDb(movieEntry: MovieEntry, status: Boolean) {
        EspressoIdlingResource.wrapEspressoIdlingResource {
            Timber.d("changeMovieStatusOnFavoriteDb($status)")
            withContext(ioDispatcher) {
                try {
                    if (status) {
                        if (!localDb.favoriteDbDao.isMovieAlreadyFavorite(movieEntry.id)) {
                            movieEntry.favoriteFlag = true
                            localDb.favoriteDbDao.insertMovieEntry(movieEntry)
                        }
                    } else {
                        localDb.favoriteDbDao.deleteFavoriteMovie(movieEntry.id)
                    }
                } catch (e: Exception) {
                    Timber.e("Failure changeMovieStatusOnFavoriteDb($status) ${e.localizedMessage}")
                }
            }
        }
    }

    /**
     * get movie details
     */
    override suspend fun getMovieDetailsInfo(movieId: Int) {
        EspressoIdlingResource.wrapEspressoIdlingResource {
            Timber.d("getMovieDetailsInfo($movieId)")
            var response: MovieDetailsResponse? = null
            withContext(ioDispatcher) {
                try {
                    response = TmdbApi.retrofitService.getMovieDetails(movieId)
                    response?.apply {
                        this.backdropPath?.let { baseImageUrl + backdropPath }
                            .also { this.backdropPath = it }
                        this.posterPath?.let { baseImageUrl + posterPath }
                            .also { this.posterPath = it }
                    }
                } catch (e: Exception) {
                    Timber.e("Failure getMovieDetailsInfo($movieId) ${e.localizedMessage}")
                }
            }

            /**
             * update UI
             */
            withContext(Dispatchers.Main) {
                _movieDetails.postValue(response!!)//TODO: check this ignore null check later
            }
        }
    }
}