package com.composebootcamp.moviesearch.repository

import androidx.lifecycle.LiveData
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse

/**
 * Interface of repository
 */
interface MovieResourceInterface {
    /**
     * get search movie result
     */
    fun getSearchResult(): LiveData<List<MovieEntry>>
    /**
     * get all favorite movies from database
     */
    fun getAllFavoriteMovies(): LiveData<List<MovieEntry>>

    /**
     * get number of favorite movies record
     */
    fun getNumOfDbRecord(): LiveData<Int>

    /**
     * get details of movie from remote
     */
    fun getMovieDetails(): LiveData<MovieDetailsResponse>

    /**
     * check an movie already in database
     */
    suspend fun isExistEntry(id: Int): Boolean

    /**
     * get list of movie by search from remote
     */
    suspend fun getMoviesByKeyword(keyword: String)

    /**
     * update database base on the favorite status
     */
    suspend fun changeMovieStatusOnFavoriteDb(movieEntry: MovieEntry, status: Boolean)
    /**
     * get movie details from remote
     */
    suspend fun getMovieDetailsInfo(movieId: Int)
}