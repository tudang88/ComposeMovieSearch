package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * regarding to https://developers.themoviedb.org/3/configuration/get-api-configuration
 * poster size should be one of the following value
 */
enum class PosterSize(val size: String) {
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
    ORIGINAL("original")
}

/**
 * A data class represent the response when get configuration
 */
@JsonClass(generateAdapter = true)
data class ImageConfigure(
    @Json(name = "base_url") val baseUrl: String,
    @Json(name = "secure_base_url") val secureBaseUrl: String,
    @Json(name = "backdrop_sizes") val backdropSizes: List<String>,
    @Json(name = "logo_sizes") val logoSizes: List<String>,
    @Json(name = "poster_sizes") val posterSizes: List<String>
) {
    /**
     * Get the base url to build full image url
     */
    fun getDefaultImageUrl(targetSize: PosterSize) = secureBaseUrl + targetSize.size
}