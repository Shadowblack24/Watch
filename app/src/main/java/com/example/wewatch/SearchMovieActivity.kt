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
import com.example.wewatch.repository.MovieRepository
import com.example.wewatch.viewmodel.SearchMovieViewModel
import com.example.wewatch.viewmodel.SearchMovieViewModelFactory

class SearchMovieActivity : AppCompatActivity() {

    private lateinit var recyclerSearchMovies: RecyclerView
    private lateinit var adapter: SearchMovieAdapter
    private lateinit var viewModel: SearchMovieViewModel

    private val apiKey = "7167c7ca"

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

        if (movieTitle.isBlank()) {
            Toast.makeText(this, "Название фильма пустое", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.searchMovies(
            apiKey = apiKey,
            title = movieTitle,
            year = movieYear.ifBlank { null }
        )
    }

    private fun setupViewModel() {
        val dao = AppDatabase.getDatabase(applicationContext).movieDao()
        val repository = MovieRepository(dao)
        val factory = SearchMovieViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SearchMovieViewModel::class.java]

        viewModel.movies.observe(this) { movies ->
            adapter.updateMovies(movies)
        }

        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}