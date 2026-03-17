package com.example.wewatch.mvi.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.usecase.DeleteSelectedMoviesUseCase
import com.example.wewatch.domain.usecase.GetAllMoviesUseCase
import com.example.wewatch.domain.usecase.UpdateMovieUseCase
import kotlinx.coroutines.launch

class MainMviViewModel(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val deleteSelectedMoviesUseCase: DeleteSelectedMoviesUseCase
) : ViewModel() {

    private val _state = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    private var moviesObserver: Observer<List<Movie>>? = null

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadMovies -> loadMovies()
            is MainIntent.ToggleMovieSelection -> updateMovieSelection(intent.movie, intent.isChecked)
            is MainIntent.DeleteSelectedMovies -> deleteSelectedMovies()
        }
    }

    private fun loadMovies() {
        _state.value = _state.value?.copy(isLoading = true)

        if (moviesObserver == null) {
            moviesObserver = Observer { movies ->
                _state.postValue(
                    _state.value?.copy(
                        movies = movies,
                        isLoading = false,
                        message = _state.value?.message
                    )
                )
            }
            getAllMoviesUseCase().observeForever(moviesObserver!!)
        }
    }

    private fun updateMovieSelection(movie: Movie, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedMovie = movie.copy(isSelectedForDelete = isChecked)
            updateMovieUseCase(updatedMovie)
        }
    }

    private fun deleteSelectedMovies() {
        viewModelScope.launch {
            val currentMovies = _state.value?.movies ?: emptyList()
            val selectedMovies = currentMovies.filter { it.isSelectedForDelete }

            if (selectedMovies.isEmpty()) {
                _state.postValue(
                    _state.value?.copy(message = "Сначала отметьте фильмы галочками")
                )
            } else {
                deleteSelectedMoviesUseCase()
                _state.postValue(
                    _state.value?.copy(message = "Выбранные фильмы удалены")
                )
            }
        }
    }

    fun clearMessage() {
        _state.value = _state.value?.copy(message = null)
    }

    override fun onCleared() {
        super.onCleared()
        moviesObserver?.let {
            getAllMoviesUseCase().removeObserver(it)
        }
    }
}