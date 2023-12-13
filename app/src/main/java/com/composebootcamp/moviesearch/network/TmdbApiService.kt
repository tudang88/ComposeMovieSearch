package com.composebootcamp.moviesearch.network

import com.composebootcamp.moviesearch.network.model.ConfigureResponse
import com.composebootcamp.moviesearch.network.model.ImagesResponse
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import com.composebootcamp.moviesearch.network.model.MovieSearchResponse
import com.composebootcamp.moviesearch.network.model.MovieTrailersResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

// prepare moshi for convert response later
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(TmdbHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 * Define service interface
 */
interface TmdbApiService {

    /**
     * get trailers
     */
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailers(@Path("movie_id") id: Int): MovieTrailersResponse
    /**
     * get the movie details
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): MovieDetailsResponse

    /**
     * get the info of images attached to the movie
     */
    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(@Path("movie_id") movieId: Int): ImagesResponse

    /**
     * get day trending movies today
     * media_type: one of all, movie, tv, person
     * time_window: day or week
     */
    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMovies(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): MovieSearchResponse

    /**
     * get configuration for image path
     */
    @GET("configuration")
    suspend fun getConfiguration(): ConfigureResponse

    /**
     * get movies list from API service
     */
    @GET("search/movie")
    suspend fun searchMoviesByName(@Query("query") searchKeyword: String): MovieSearchResponse
}

/**
 * The api service will be an singleton
 * and share among project
 */
object TmdbApi {
    val retrofitService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}