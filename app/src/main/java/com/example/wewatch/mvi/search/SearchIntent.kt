package com.example.wewatch.mvi.search

sealed class SearchIntent {
    data class SearchMovies(val title: String, val year: String?) : SearchIntent()
}