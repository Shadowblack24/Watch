package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepositoryContract

class InsertMovieUseCase(
    private val repository: MovieRepositoryContract
) {
    suspend operator fun invoke(movie: Movie) {
        repository.insertMovie(movie)
    }
}