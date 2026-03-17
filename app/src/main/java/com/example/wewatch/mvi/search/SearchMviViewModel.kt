package com.example.wewatch.mvi.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.launch

class SearchMviViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _state = MutableLiveData(SearchState())
    val state: LiveData<SearchState> = _state

    fun handleIntent(intent: SearchIntent, apiKey: String) {

        when (intent) {

            is SearchIntent.SearchMovies -> searchMovies(apiKey, intent.title, intent.year)

        }

    }

    private fun searchMovies(apiKey: String, title: String, year: String?) {

        _state.value = _state.value?.copy(isLoading = true)

        viewModelScope.launch {

            try {

                val movies = searchMoviesUseCase(apiKey, title, year)

                if (movies.isEmpty()) {

                    _state.postValue(
                        _state.value?.copy(
                            isLoading = false,
                            message = "Фильмы не найдены"
                        )
                    )

                } else {

                    _state.postValue(
                        _state.value?.copy(
                            movies = movies,
                            isLoading = false
                        )
                    )

                }

            } catch (e: Exception) {

                _state.postValue(
                    _state.value?.copy(
                        isLoading = false,
                        message = "Ошибка сети"
                    )
                )

            }

        }

    }

    fun clearMessage() {
        _state.value = _state.value?.copy(message = null)
    }
}