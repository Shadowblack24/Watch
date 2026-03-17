package com.example.wewatch.mvi.search

import com.example.wewatch.network.OmdbMovieDetailsDto

data class SearchState(
    val movies: List<OmdbMovieDetailsDto> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)