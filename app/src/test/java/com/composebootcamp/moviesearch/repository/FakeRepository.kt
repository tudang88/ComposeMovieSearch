package com.composebootcamp.moviesearch.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import kotlinx.coroutines.runBlocking

/**
 * Fake repository use for local test
 */
class FakeRepository(favoriteMoviesList: MutableList<MovieEntry> = mutableListOf()) :
    MovieResourceInterface {
    /**
     * fake remote data for local test the method which need to get from retrofit
     */
    var moviesDataForSearch: LinkedHashMap<String, MovieEntry> = LinkedHashMap()
    var movieDetailsData: LinkedHashMap<Int, MovieDetailsResponse> = LinkedHashMap()
    private val _searchResult = MutableLiveData<List<MovieEntry>>()
    override fun getSearchResult() = _searchResult

    /**
     * live data maintain number of database record
     * this number will be use to update favorite badge
     */
    private val _allFavoriteMovies = MutableLiveData<List<MovieEntry>>()
    override fun getAllFavoriteMovies() = _allFavoriteMovies
    override fun getNumOfDbRecord() = _allFavoriteMovies.map { it.size }

    private val _movieDetails = MutableLiveData<MovieDetailsResponse>()
    override fun getMovieDetails() = _movieDetails


    /**
     * The function only use for test
     */
    fun setupMoviesDataForSearch(vararg movieEntry: MovieEntry) {
        for (movie in movieEntry) {
            moviesDataForSearch.put(movie.title, movie)
        }
    }

    /**
     * The function only use for test
     */
    fun setupMoviesDataDetails(vararg movieDetails: MovieDetailsResponse) {
        for (movieDetail in movieDetails) {
            movieDetailsData.put(movieDetail.id, movieDetail)
        }
    }

    init {
        // initialize test database
        _allFavoriteMovies.value = favoriteMoviesList
    }

    /**
     * check availability of a record
     */
    override suspend fun isExistEntry(id: Int): Boolean {
        return if (_allFavoriteMovies.value.isNullOrEmpty()) {
            false
        } else {
            val record = _allFavoriteMovies.value!!.filter { it.id == id }
            record.isNotEmpty()
        }
    }

    /**
     * search movies from remote
     */
    override suspend fun getMoviesByKeyword(keyword: String) {
        runBlocking {
            val resultList = moviesDataForSearch.filter { it.key.contains(keyword) }
            val foundMovies = mutableListOf<MovieEntry>()
            for (movie in resultList.values) {
                foundMovies.add(movie)
            }
            _searchResult.value = foundMovies
        }
    }

    /**
     * Update database base on favorite status
     */
    override suspend fun changeMovieStatusOnFavoriteDb(movieEntry: MovieEntry, status: Boolean) {
        runBlocking {
            val favoriteDb = (_allFavoriteMovies.value ?: emptyList()).toMutableList()
            if (status) {
                //add when user press favorite
                movieEntry.favoriteFlag = true
                if (!favoriteDb.contains(movieEntry)) {
                    favoriteDb.add(movieEntry)
                }
            } else {
                // remove
                favoriteDb.removeIf { it.id == movieEntry.id }
            }
            _allFavoriteMovies.value = favoriteDb
        }
    }

    /**
     * get movie detail by movieId from remote
     */
    override suspend fun getMovieDetailsInfo(movieId: Int) {
        runBlocking {
            _movieDetails.value = movieDetailsData.getValue(movieId)
        }
    }
}