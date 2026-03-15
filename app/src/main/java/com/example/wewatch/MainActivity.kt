package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wewatch.adapter.MovieAdapter
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.repository.MovieRepository
import com.example.wewatch.viewmodel.MainViewModel
import com.example.wewatch.viewmodel.MainViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var fabAddMovie: FloatingActionButton
    private lateinit var btnDeleteSelected: Button
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "WeWatch"

        fabAddMovie = findViewById(R.id.fabAddMovie)
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        recyclerMovies = findViewById(R.id.recyclerMovies)

        setupRecyclerView()
        setupViewModel()

        fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }

        btnDeleteSelected.setOnClickListener {
            val currentMovies = movieAdapter.getCurrentMovies()
            val hasSelected = currentMovies.any { it.isSelectedForDelete }

            if (!hasSelected) {
                Toast.makeText(this, "Сначала отметьте фильмы галочками", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.deleteSelectedMovies()
                Toast.makeText(this, "Выбранные фильмы удалены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(mutableListOf()) { movie, isChecked ->
            viewModel.updateMovieSelection(movie, isChecked)
        }

        recyclerMovies.layoutManager = LinearLayoutManager(this)
        recyclerMovies.adapter = movieAdapter
    }

    private fun setupViewModel() {
        val dao = AppDatabase.getDatabase(applicationContext).movieDao()
        val repository = MovieRepository(dao)
        val factory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.movies.observe(this) { movies ->
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