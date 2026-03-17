package com.example.wewatch.mvi.main

import com.example.wewatch.data.MovieEntity

data class MainState(
    val movies: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)