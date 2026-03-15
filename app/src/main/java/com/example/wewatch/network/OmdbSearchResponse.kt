package com.example.wewatch.network

import com.google.gson.annotations.SerializedName

data class OmdbSearchResponse(
    @SerializedName("Search")
    val search: List<OmdbMovieDto>? = null,

    @SerializedName("Response")
    val response: String,

    @SerializedName("Error")
    val error: String? = null
)