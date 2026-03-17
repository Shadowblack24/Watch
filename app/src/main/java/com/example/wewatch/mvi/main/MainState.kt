package com.example.wewatch.mvi.main

import com.example.wewatch.domain.model.Movie

data class MainState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)