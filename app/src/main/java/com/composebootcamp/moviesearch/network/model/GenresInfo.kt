package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.JsonClass

/**
 * The data class represent genres info inside a response from
 * GET /movie/{movie_id}
 */
@JsonClass(generateAdapter = true)
class GenresInfo (
    val id: Int,
    val name: String
)
