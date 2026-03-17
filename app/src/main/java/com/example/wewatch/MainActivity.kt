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
import com.example.wewatch.data.repository.MovieRepositoryImpl
import com.example.wewatch.domain.usecase.DeleteSelectedMoviesUseCase
import com.example.wewatch.domain.usecase.GetAllMoviesUseCase
import com.example.wewatch.domain.usecase.UpdateMovieUseCase
import com.example.wewatch.mvi.main.MainIntent
import com.example.wewatch.mvi.main.MainMviViewModel
import com.example.wewatch.mvi.main.MainMviViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var fabAddMovie: FloatingActionButton
    private lateinit var btnDeleteSelected: Button
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MainMviViewModel

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
        setupListeners()

        viewModel.handleIntent(MainIntent.LoadMovies)
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(mutableListOf()) { movie, isChecked ->
            viewModel.handleIntent(MainIntent.ToggleMovieSelection(movie, isChecked))
        }

        recyclerMovies.layoutManager = LinearLayoutManager(this)
        recyclerMovies.adapter = movieAdapter
    }

    private fun setupViewModel() {
        val dao = AppDatabase.getDatabase(applicationContext).movieDao()
        val repository = MovieRepositoryImpl(dao)

        val getAllMoviesUseCase = GetAllMoviesUseCase(repository)
        val updateMovieUseCase = UpdateMovieUseCase(repository)
        val deleteSelectedMoviesUseCase = DeleteSelectedMoviesUseCase(repository)

        val factory = MainMviViewModelFactory(
            getAllMoviesUseCase,
            updateMovieUseCase,
            deleteSelectedMoviesUseCase
        )

        viewModel = ViewModelProvider(this, factory)[MainMviViewModel::class.java]

        viewModel.state.observe(this) { state ->
            renderState(state)
        }
    }

    private fun setupListeners() {
        fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }

        btnDeleteSelected.setOnClickListener {
            viewModel.handleIntent(MainIntent.DeleteSelectedMovies)
        }
    }

    private fun renderState(state: com.example.wewatch.mvi.main.MainState) {
        if (state.movies.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            recyclerMovies.visibility = View.GONE
            btnDeleteSelected.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            recyclerMovies.visibility = View.VISIBLE
            btnDeleteSelected.visibility = View.VISIBLE
            movieAdapter.updateMovies(state.movies)
        }

        state.message?.let { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleIntent(MainIntent.LoadMovies)
    }
}