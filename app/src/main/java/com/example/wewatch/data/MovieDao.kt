package com.example.wewatch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY id DESC")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert
    suspend fun insertMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE isSelectedForDelete = 1")
    suspend fun deleteSelectedMovies()
}