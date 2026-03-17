package com.example.wewatch.mvi.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.repository.MovieRepository

class SearchMviViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchMviViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchMviViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}