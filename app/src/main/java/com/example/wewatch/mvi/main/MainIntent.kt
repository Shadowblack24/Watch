package com.example.wewatch.mvi.main

import com.example.wewatch.data.MovieEntity

sealed class MainIntent {
    data object LoadMovies : MainIntent()
    data class ToggleMovieSelection(val movie: MovieEntity, val isChecked: Boolean) : MainIntent()
    data object DeleteSelectedMovies : MainIntent()
}