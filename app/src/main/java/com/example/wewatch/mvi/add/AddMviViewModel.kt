package com.example.wewatch.mvi.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.MovieEntity
import com.example.wewatch.repository.MovieRepository
import kotlinx.coroutines.launch

class AddMviViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableLiveData(AddState())
    val state: LiveData<AddState> = _state

    fun handleIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.SearchMovie -> {
                if (intent.title.isBlank()) {
                    _state.value = _state.value?.copy(
                        message = "Введите название фильма для поиска"
                    )
                }
            }

            is AddIntent.SelectMovie -> {
                _state.value = _state.value?.copy(
                    title = intent.title,
                    year = intent.year,
                    posterUrl = intent.posterUrl,
                    genre = intent.genre,
                    message = "Фильм выбран"
                )
            }

            is AddIntent.AddMovieToDatabase -> addMovie()
        }
    }

    private fun addMovie() {
        val currentState = _state.value ?: AddState()

        if (currentState.title.isBlank()) {
            _state.value = currentState.copy(
                message = "Введите название фильма"
            )
            return
        }

        viewModelScope.launch {
            val movie = MovieEntity(
                title = currentState.title,
                year = currentState.year,
                posterUrl = currentState.posterUrl,
                genre = currentState.genre
            )

            repository.insertMovie(movie)

            _state.postValue(
                currentState.copy(
                    isMovieAdded = true,
                    message = "Фильм добавлен"
                )
            )
        }
    }

    fun clearMessage() {
        _state.value = _state.value?.copy(message = null)
    }

    fun resetMovieAddedFlag() {
        _state.value = _state.value?.copy(isMovieAdded = false)
    }
}