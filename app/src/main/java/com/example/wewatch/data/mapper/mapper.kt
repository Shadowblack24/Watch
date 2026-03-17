package com.example.wewatch.data.mapper

import com.example.wewatch.data.MovieEntity
import com.example.wewatch.domain.model.Movie

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        year = year,
        posterUrl = posterUrl,
        genre = genre,
        isSelectedForDelete = isSelectedForDelete
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        year = year,
        posterUrl = posterUrl,
        genre = genre,
        isSelectedForDelete = isSelectedForDelete
    )
}