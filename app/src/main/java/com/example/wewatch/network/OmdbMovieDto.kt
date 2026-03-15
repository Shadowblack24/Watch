package com.example.wewatch.network

import com.google.gson.annotations.SerializedName

data class OmdbMovieDto(
    @SerializedName("Title")
    val title: String,

    @SerializedName("Year")
    val year: String,

    @SerializedName("Poster")
    val poster: String,

    @SerializedName("imdbID")
    val imdbId: String
)