package com.example.wewatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.MovieEntity
import com.example.wewatch.repository.MovieRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    val movies: LiveData<List<MovieEntity>> = repository.allMovies

    fun updateMovieSelection(movie: MovieEntity, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedMovie = movie.copy(isSelectedForDelete = isChecked)
            repository.updateMovie(updatedMovie)
        }
    }

    fun deleteSelectedMovies() {
        viewModelScope.launch {
            repository.deleteSelectedMovies()
        }
    }
}