package com.example.wewatch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val year: String,

    val posterUrl: String = "",

    val genre: String = "",

    val isSelectedForDelete: Boolean = false
)