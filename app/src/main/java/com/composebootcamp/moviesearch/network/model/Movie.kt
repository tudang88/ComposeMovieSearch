package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The data class represent for one entry in the result list when search by movie name
 */
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "release_date") val releaseDate: String,
    val title: String,
    val id: Int,
    val video: Boolean,
    val overview: String,
    @Json(name = "vote_average") val voteAverage: Float
)
