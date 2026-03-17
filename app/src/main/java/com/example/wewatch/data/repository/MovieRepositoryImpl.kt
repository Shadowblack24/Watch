package com.example.wewatch.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.wewatch.data.MovieDao
import com.example.wewatch.data.MovieEntity
import com.example.wewatch.data.mapper.toDomain
import com.example.wewatch.data.mapper.toEntity
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepositoryContract
import com.example.wewatch.network.OmdbMovieDetailsDto
import com.example.wewatch.network.RetrofitClient

class MovieRepositoryImpl(
    private val movieDao: MovieDao
) : MovieRepositoryContract {

    override fun getAllMoviesLive(): LiveData<List<Movie>> {
        val result = MediatorLiveData<List<Movie>>()

        result.addSource(movieDao.getAllMoviesLive()) { entities: List<MovieEntity> ->
            result.value = entities.map { entity -> entity.toDomain() }
        }

        return result
    }

    override suspend fun getAllMoviesOnce(): List<Movie> {
        return movieDao.getAllMovies().map { entity -> entity.toDomain() }
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    override suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(movie.toEntity())
    }

    override suspend fun deleteSelectedMovies() {
        movieDao.deleteSelectedMovies()
    }

    override suspend fun searchMovies(
        apiKey: String,
        title: String,
        year: String?
    ): List<OmdbMovieDetailsDto> {
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