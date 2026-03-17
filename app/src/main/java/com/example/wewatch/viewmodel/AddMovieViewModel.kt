package com.example.wewatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.MovieEntity
import com.example.wewatch.repository.MovieRepository
import kotlinx.coroutines.launch

class AddMovieViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieAdded = MutableLiveData<Boolean>()
    val movieAdded: LiveData<Boolean> = _movieAdded

    fun insertMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repository.insertMovie(movie)
            _movieAdded.value = true
        }
    }

    fun resetMovieAddedState() {
        _movieAdded.value = false
    }
}