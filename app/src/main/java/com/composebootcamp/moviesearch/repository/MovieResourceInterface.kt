package com.composebootcamp.moviesearch.repository

import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface of repository
 */
interface MovieResourceInterface {
    /**
     * get search movie result
     */
    fun getSearchResult(): Flow<List<MovieEntry>>

    /**
     * get all favorite movies from database
     */
    fun getAllFavoriteMovies(): Flow<List<MovieEntry>>

    /**
     * get number of favorite movies record
     */
    fun getNumOfDbRecord(): Flow<Int>

    /**
     * get details of movie from remote
     */
    fun getMovieDetails(): StateFlow<MovieDetailsResponse?>

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