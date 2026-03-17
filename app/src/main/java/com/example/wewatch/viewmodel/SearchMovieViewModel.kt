package com.example.wewatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.network.OmdbMovieDetailsDto
import com.example.wewatch.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchMovieViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableLiveData<List<OmdbMovieDetailsDto>>()
    val movies: LiveData<List<OmdbMovieDetailsDto>> = _movies

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun searchMovies(apiKey: String, title: String, year: String?) {
        viewModelScope.launch {
            try {
                val result = repository.searchMovies(apiKey, title, year)
                if (result.isEmpty()) {
                    _errorMessage.value = "Фильмы не найдены"
                } else {
                    _movies.value = result
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети: ${e.message}"
            }
        }
    }
}