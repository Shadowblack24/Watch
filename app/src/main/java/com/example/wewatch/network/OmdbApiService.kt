package com.example.wewatch.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {

    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") title: String,
        @Query("y") year: String? = null
    ): OmdbSearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String
    ): OmdbMovieDetailsDto
}