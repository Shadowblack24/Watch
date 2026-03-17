package com.example.wewatch.mvi.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.domain.usecase.DeleteSelectedMoviesUseCase
import com.example.wewatch.domain.usecase.GetAllMoviesUseCase
import com.example.wewatch.domain.usecase.UpdateMovieUseCase

class MainMviViewModelFactory(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val deleteSelectedMoviesUseCase: DeleteSelectedMoviesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainMviViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainMviViewModel(
                getAllMoviesUseCase,
                updateMovieUseCase,
                deleteSelectedMoviesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}