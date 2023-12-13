package com.composebootcamp.moviesearch.network

import okhttp3.OkHttpClient

import timber.log.Timber
import com.composebootcamp.moviesearch.BuildConfig
class TmdbHttpClient: OkHttpClient() {

    companion object {

        private const val API_KEY = BuildConfig.TMDB_API_KEY

        fun getClient(): OkHttpClient {
            Timber.d("get OkHttpClient")
            return Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url()
                        .newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    Timber.tag("TmDbHttpClient").d("Request:$request")
                    chain.proceed(request)
                }
                .build()
        }

    }

}
