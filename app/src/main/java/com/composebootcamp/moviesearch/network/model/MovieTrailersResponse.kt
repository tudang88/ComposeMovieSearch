package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieTrailersResponse(
    val id: Int,
    val results: List<TrailerInfo>
)
