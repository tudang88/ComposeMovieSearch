package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrailerInfo(
    val key: String,
    val site: String
)
