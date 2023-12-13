package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.JsonClass

/**
 * The data class represent the response of HTTP GET
 * /movie/{movie_id}/images request
 */
@JsonClass(generateAdapter = true)
data class ImagesResponse(
    val id: Int,
    val backdrops: List<MovieImageInfo>,
    val posters: List<MovieImageInfo>
)
