package com.example.wewatch.domain.model

data class Movie(
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String = "",
    val genre: String = "",
    val isSelectedForDelete: Boolean = false
)