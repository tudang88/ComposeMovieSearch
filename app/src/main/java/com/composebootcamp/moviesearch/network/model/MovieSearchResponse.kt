package com.composebootcamp.moviesearch.network.model

import com.composebootcamp.moviesearch.network.model.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The data class represent a response content from the movie database API
 */
@JsonClass(generateAdapter = true)
data class MovieSearchResponse(
    val page: Int,
    @Json(name = "results") val movieList: List<Movie>
)