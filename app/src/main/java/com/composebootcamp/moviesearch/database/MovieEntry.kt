package com.composebootcamp.moviesearch.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
/**
 * Each entry of favorite table will be one instance of
 * this data class
 */
@JsonClass(generateAdapter = true)
@Entity(tableName = "favorite_table")
@Parcelize
data class MovieEntry(
    // this is full meaning url which can be used for retrieve real poster
    @PrimaryKey
    val id: Int,
    val posterUrl: String,
    val title: String,
    val releaseDate: String,
    val overview: String,
    var favoriteFlag: Boolean = false
) : Parcelable
