package com.composebootcamp.moviesearch.network.model

import com.composebootcamp.moviesearch.database.MovieEntry
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The data class represent the content of response from
 * GET /movie/{movie_id}
 */
@JsonClass(generateAdapter = true)
data class MovieDetailsResponse(
    val adult: Boolean,
    @Json(name = "backdrop_path") var backdropPath: String?,
    val genres: List<GenresInfo>,
    val homepage: String?,
    val id: Int,
    @Json(name = "poster_path") var posterPath: String?,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    val overview: String?,
    @Json(name = "release_date") val releaseDate: String,
    val runtime: Int?,
    val title: String,
    @Json(name = "vote_average") val rating: Float
)

/**
 * support method
 */
fun List<GenresInfo>.getAllGenres(): String {
    if (isEmpty()) return "Unknown"
    var result = get(0).name
    for (index in 1 until size) {
        result += ", " + get(index).name
    }
    return result
}

/**
 * extend function support convert Detail Response to MovieEntry for insert to database
 */
fun MovieDetailsResponse.convertToMovieEntry(): MovieEntry {
    return MovieEntry(id, posterPath ?: "", title, releaseDate, overview ?: "Unavailable")
}
