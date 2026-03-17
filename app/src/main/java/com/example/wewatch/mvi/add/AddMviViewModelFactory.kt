package com.example.wewatch.mvi.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.repository.MovieRepository

class AddMviViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddMviViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddMviViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}