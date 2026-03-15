package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wewatch.adapter.MovieAdapter
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var fabAddMovie: FloatingActionButton
    private lateinit var btnDeleteSelected: Button
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "WeWatch"

        fabAddMovie = findViewById(R.id.fabAddMovie)
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        recyclerMovies = findViewById(R.id.recyclerMovies)

        setupRecyclerView()

        fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }

        btnDeleteSelected.setOnClickListener {
            deleteSelectedMovies()
        }
    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(mutableListOf()) { movie, isChecked ->
            updateMovieSelection(movie, isChecked)
        }

        recyclerMovies.layoutManager = LinearLayoutManager(this)
        recyclerMovies.adapter = movieAdapter
    }

    private fun loadMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            val movies = AppDatabase.getDatabase(this@MainActivity)
                .movieDao()
                .getAllMovies()

            withContext(Dispatchers.Main) {
                if (movies.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                    recyclerMovies.visibility = View.GONE
                    btnDeleteSelected.visibility = View.GONE
                } else {
                    emptyStateLayout.visibility = View.GONE
                    recyclerMovies.visibility = View.VISIBLE
                    btnDeleteSelected.visibility = View.VISIBLE
                    movieAdapter.updateMovies(movies)
                }
            }
        }
    }

    private fun updateMovieSelection(movie: MovieEntity, isChecked: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val updatedMovie = movie.copy(isSelectedForDelete = isChecked)
            AppDatabase.getDatabase(this@MainActivity)
                .movieDao()
                .updateMovie(updatedMovie)
        }
    }

    private fun deleteSelectedMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(this@MainActivity)
                .movieDao()
                .deleteSelectedMovies()

            val movies = AppDatabase.getDatabase(this@MainActivity)
                .movieDao()
                .getAllMovies()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity,
                    "Выбранные фильмы удалены",
                    Toast.LENGTH_SHORT
                ).show()

                if (movies.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                    recyclerMovies.visibility = View.GONE
                    btnDeleteSelected.visibility = View.GONE
                } else {
                    emptyStateLayout.visibility = View.GONE
                    recyclerMovies.visibility = View.VISIBLE
                    btnDeleteSelected.visibility = View.VISIBLE
                    movieAdapter.updateMovies(movies)
                }
            }
        }
    }
}