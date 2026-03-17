package com.example.wewatch.domain.usecase

import androidx.lifecycle.LiveData
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepositoryContract

class GetAllMoviesUseCase(
    private val repository: MovieRepositoryContract
) {
    operator fun invoke(): LiveData<List<Movie>> {
        return repository.getAllMoviesLive()
    }
}