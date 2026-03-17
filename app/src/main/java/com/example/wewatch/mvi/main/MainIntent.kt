package com.example.wewatch.mvi.main

import com.example.wewatch.domain.model.Movie

sealed class MainIntent {
    data object LoadMovies : MainIntent()
    data class ToggleMovieSelection(val movie: Movie, val isChecked: Boolean) : MainIntent()
    data object DeleteSelectedMovies : MainIntent()
}