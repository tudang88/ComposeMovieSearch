package com.composebootcamp.moviesearch.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The data class represent a response content from the movie database API
 */
@JsonClass(generateAdapter = true)
data class ConfigureResponse(
    @Json(name = "images") val images: ImageConfigure

)