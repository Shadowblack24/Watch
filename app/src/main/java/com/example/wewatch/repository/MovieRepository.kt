package com.example.wewatch.repository

import androidx.lifecycle.LiveData
import com.example.wewatch.data.MovieDao
import com.example.wewatch.data.MovieEntity
import com.example.wewatch.network.OmdbMovieDetailsDto
import com.example.wewatch.network.RetrofitClient

class MovieRepository(
    private val movieDao: MovieDao
) {

    val allMovies: LiveData<List<MovieEntity>> = movieDao.getAllMoviesLive()

    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun updateMovie(movie: MovieEntity) {
        movieDao.updateMovie(movie)
    }

    suspend fun deleteSelectedMovies() {
        movieDao.deleteSelectedMovies()
    }

    suspend fun getAllMoviesOnce(): List<MovieEntity> {
        return movieDao.getAllMovies()
    }

    suspend fun searchMovies(apiKey: String, title: String, year: String?): List<OmdbMovieDetailsDto> {
        val response = RetrofitClient.api.searchMovies(
            apiKey = apiKey,
            title = title,
            year = year
        )

        if (response.response == "True" && !response.search.isNullOrEmpty()) {
            return response.search.map { movie ->
                try {
                    RetrofitClient.api.getMovieDetails(
                        apiKey = apiKey,
                        imdbId = movie.imdbId
                    )
                } catch (e: Exception) {
                    OmdbMovieDetailsDto(
                        title = movie.title,
                        year = movie.year,
                        poster = movie.poster,
                        imdbId = movie.imdbId,
                        genre = ""
                    )
                }
            }
        } else {
            return emptyList()
        }
    }
}