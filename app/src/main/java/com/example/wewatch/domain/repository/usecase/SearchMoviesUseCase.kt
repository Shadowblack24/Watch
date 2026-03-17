package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.repository.MovieRepositoryContract
import com.example.wewatch.network.OmdbMovieDetailsDto

class SearchMoviesUseCase(
    private val repository: MovieRepositoryContract
) {
    suspend operator fun invoke(
        apiKey: String,
        title: String,
        year: String?
    ): List<OmdbMovieDetailsDto> {
        return repository.searchMovies(apiKey, title, year)
    }
}