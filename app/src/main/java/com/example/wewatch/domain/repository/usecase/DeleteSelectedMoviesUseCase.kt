package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.repository.MovieRepositoryContract

class DeleteSelectedMoviesUseCase(
    private val repository: MovieRepositoryContract
) {
    suspend operator fun invoke() {
        repository.deleteSelectedMovies()
    }
}