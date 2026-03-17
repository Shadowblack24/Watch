package com.example.wewatch.mvi.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.domain.usecase.SearchMoviesUseCase

class SearchMviViewModelFactory(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchMviViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchMviViewModel(searchMoviesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}