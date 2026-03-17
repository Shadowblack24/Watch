package com.example.wewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wewatch.adapter.SearchMovieAdapter
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.repository.MovieRepositoryImpl
import com.example.wewatch.domain.usecase.SearchMoviesUseCase
import com.example.wewatch.mvi.search.SearchIntent
import com.example.wewatch.mvi.search.SearchMviViewModel
import com.example.wewatch.mvi.search.SearchMviViewModelFactory
import com.example.wewatch.repository.MovieRepository

class SearchMovieActivity : AppCompatActivity() {

    private lateinit var recyclerSearchMovies: RecyclerView
    private lateinit var adapter: SearchMovieAdapter
    private lateinit var viewModel: SearchMviViewModel

    private val apiKey = "ТВОЙ_API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        title = "Search Movies"

        recyclerSearchMovies = findViewById(R.id.recyclerSearchMovies)

        adapter = SearchMovieAdapter(emptyList()) { movie ->

            val resultIntent = Intent().apply {
                putExtra("selected_title", movie.title)
                putExtra("selected_year", movie.year)
                putExtra("selected_poster", movie.poster)
                putExtra("selected_genre", movie.genre)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        recyclerSearchMovies.layoutManager = LinearLayoutManager(this)
        recyclerSearchMovies.adapter = adapter

        setupViewModel()

        val movieTitle = intent.getStringExtra("movie_title") ?: ""
        val movieYear = intent.getStringExtra("movie_year") ?: ""

        viewModel.handleIntent(
            SearchIntent.SearchMovies(movieTitle, movieYear.ifBlank { null }),
            apiKey
        )
    }

    private fun setupViewModel() {

        val dao = AppDatabase.getDatabase(applicationContext).movieDao()
        val repository = MovieRepositoryImpl(dao)

        val searchMoviesUseCase = SearchMoviesUseCase(repository)

        val factory = SearchMviViewModelFactory(searchMoviesUseCase)

        viewModel = ViewModelProvider(this, factory)[SearchMviViewModel::class.java]

        viewModel.state.observe(this) { state ->

            adapter.updateMovies(state.movies)

            state.message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearMessage()
            }

        }

    }
}