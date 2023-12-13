package com.example.moviessearch.utils

/**
 * convert runtime from minute to string
 */
fun convertTimeToString(runtime: Int?): String {
    var result = ""
    runtime?.let {
        result = (it / 60).toString() + "h" + (it % 60)
    }
    return result
}