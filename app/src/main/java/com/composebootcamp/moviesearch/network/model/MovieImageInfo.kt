package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The data class represent for image info in the response of
 * get/movie/{movie_id}/images
 */
@JsonClass(generateAdapter = true)
data class MovieImageInfo(
    @Json(name = "aspect_ratio") val aspectRatio: Float,
    @Json(name = "file_path") val filePath: String,
    val height: Int,
    val width: Int
)
