package com.example.wewatch.mvi.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.repository.MovieRepository

class MainMviViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainMviViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainMviViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}