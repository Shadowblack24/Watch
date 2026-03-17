package com.example.wewatch.domain.repository

import androidx.lifecycle.LiveData
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.network.OmdbMovieDetailsDto

interface MovieRepositoryContract {

    fun getAllMoviesLive(): LiveData<List<Movie>>

    suspend fun getAllMoviesOnce(): List<Movie>

    suspend fun insertMovie(movie: Movie)

    suspend fun updateMovie(movie: Movie)

    suspend fun deleteSelectedMovies()

    suspend fun searchMovies(apiKey: String, title: String, year: String?): List<OmdbMovieDetailsDto>
}