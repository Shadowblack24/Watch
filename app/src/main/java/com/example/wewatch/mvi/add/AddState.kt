package com.example.wewatch.mvi.add

data class AddState(
    val title: String = "",
    val year: String = "",
    val posterUrl: String = "",
    val genre: String = "",
    val isMovieAdded: Boolean = false,
    val message: String? = null
)