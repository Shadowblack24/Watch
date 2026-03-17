package com.example.wewatch.mvi.add

sealed class AddIntent {
    data class SearchMovie(val title: String, val year: String?) : AddIntent()
    data class SelectMovie(
        val title: String,
        val year: String,
        val posterUrl: String,
        val genre: String
    ) : AddIntent()
    data object AddMovieToDatabase : AddIntent()
}