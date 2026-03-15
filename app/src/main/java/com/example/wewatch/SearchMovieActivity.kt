package com.example.wewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wewatch.adapter.SearchMovieAdapter
import com.example.wewatch.network.OmdbMovieDetailsDto
import com.example.wewatch.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchMovieActivity : AppCompatActivity() {

    private lateinit var recyclerSearchMovies: RecyclerView
    private lateinit var adapter: SearchMovieAdapter

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

        val movieTitle = intent.getStringExtra("movie_title") ?: ""
        val movieYear = intent.getStringExtra("movie_year") ?: ""

        if (movieTitle.isBlank()) {
            Toast.makeText(this, "Название фильма пустое", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        searchMovies(movieTitle, movieYear)
    }

    private fun searchMovies(title: String, year: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.searchMovies(
                    apiKey = apiKey,
                    title = title,
                    year = year.ifBlank { null }
                )

                if (response.response == "True" && !response.search.isNullOrEmpty()) {
                    val detailedMovies = response.search.map { movie ->
                        async {
                            try {
                                RetrofitClient.api.getMovieDetails(
                                    apiKey = apiKey,
                                    imdbId = movie.imdbId
                                )
                            } catch (e: Exception) {
                                OmdbMovieDetailsDto(
                                    title = movie.title,
                                    year = movie.year,
                                    poster = movie.poster,
                                    imdbId = movie.imdbId,
                                    genre = ""
                                )
                            }
                        }
                    }.awaitAll()

                    withContext(Dispatchers.Main) {
                        adapter.updateMovies(detailedMovies)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@SearchMovieActivity,
                            response.error ?: "Фильмы не найдены",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SearchMovieActivity,
                        "Ошибка сети: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}