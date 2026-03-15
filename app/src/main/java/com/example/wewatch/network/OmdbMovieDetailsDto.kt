package com.example.wewatch.network

import com.google.gson.annotations.SerializedName

data class OmdbMovieDetailsDto(
    @SerializedName("Title")
    val title: String = "",

    @SerializedName("Year")
    val year: String = "",

    @SerializedName("Poster")
    val poster: String = "",

    @SerializedName("Genre")
    val genre: String = "",

    @SerializedName("imdbID")
    val imdbId: String = "",

    @SerializedName("Response")
    val response: String = "",

    @SerializedName("Error")
    val error: String? = null
)