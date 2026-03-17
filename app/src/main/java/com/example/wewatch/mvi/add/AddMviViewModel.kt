package com.example.wewatch.mvi.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.usecase.InsertMovieUseCase
import kotlinx.coroutines.launch

class AddMviViewModel(
    private val insertMovieUseCase: InsertMovieUseCase
) : ViewModel() {

    private val _state = MutableLiveData(AddState())
    val state: LiveData<AddState> = _state

    fun handleIntent(intent: AddIntent) {
        when (intent) {

            is AddIntent.SearchMovie -> {
                if (intent.title.isBlank()) {
                    _state.value = _state.value?.copy(
                        message = "Введите название фильма"
                    )
                }
            }

            is AddIntent.SelectMovie -> {
                _state.value = _state.value?.copy(
                    title = intent.title,
                    year = intent.year,
                    posterUrl = intent.posterUrl,
                    genre = intent.genre
                )
            }

            is AddIntent.AddMovieToDatabase -> addMovie()
        }
    }

    private fun addMovie() {

        val current = _state.value ?: return

        if (current.title.isBlank()) {
            _state.value = current.copy(
                message = "Введите название фильма"
            )
            return
        }

        viewModelScope.launch {

            val movie = Movie(
                title = current.title,
                year = current.year,
                posterUrl = current.posterUrl,
                genre = current.genre
            )

            insertMovieUseCase(movie)

            _state.postValue(
                current.copy(
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