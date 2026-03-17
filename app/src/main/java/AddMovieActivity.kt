package com.example.wewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.repository.MovieRepositoryImpl
import com.example.wewatch.domain.usecase.InsertMovieUseCase
import com.example.wewatch.mvi.add.AddIntent
import com.example.wewatch.mvi.add.AddMviViewModel
import com.example.wewatch.mvi.add.AddMviViewModelFactory
import com.example.wewatch.repository.MovieRepository

class AddMovieActivity : AppCompatActivity() {

    private lateinit var etMovieTitle: EditText
    private lateinit var etMovieYear: EditText
    private lateinit var ivSelectedPoster: ImageView
    private lateinit var btnSearchMovie: Button
    private lateinit var btnAddMovie: Button
    private lateinit var viewModel: AddMviViewModel

    private val searchMovieLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedTitle = data?.getStringExtra("selected_title") ?: ""
                val selectedYear = data?.getStringExtra("selected_year") ?: ""
                val selectedPoster = data?.getStringExtra("selected_poster") ?: ""
                val selectedGenre = data?.getStringExtra("selected_genre") ?: ""

                viewModel.handleIntent(
                    AddIntent.SelectMovie(
                        title = selectedTitle,
                        year = selectedYear,
                        posterUrl = selectedPoster,
                        genre = selectedGenre
                    )
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        title = "Add Movie"

        etMovieTitle = findViewById(R.id.etMovieTitle)
        etMovieYear = findViewById(R.id.etMovieYear)
        ivSelectedPoster = findViewById(R.id.ivSelectedPoster)
        btnSearchMovie = findViewById(R.id.btnSearchMovie)
        btnAddMovie = findViewById(R.id.btnAddMovie)

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        val dao = AppDatabase.getDatabase(applicationContext).movieDao()
        val repository = MovieRepositoryImpl(dao)

        val insertMovieUseCase = InsertMovieUseCase(repository)

        val factory = AddMviViewModelFactory(insertMovieUseCase)

        viewModel = ViewModelProvider(this, factory)[AddMviViewModel::class.java]

        viewModel.state.observe(this) { state ->
            renderState(state)
        }
    }

    private fun setupListeners() {
        btnSearchMovie.setOnClickListener {
            val movieTitle = etMovieTitle.text.toString().trim()
            val movieYear = etMovieYear.text.toString().trim()

            viewModel.handleIntent(
                AddIntent.SearchMovie(
                    title = movieTitle,
                    year = movieYear.ifBlank { null }
                )
            )

            if (movieTitle.isNotBlank()) {
                val intent = Intent(this, SearchMovieActivity::class.java).apply {
                    putExtra("movie_title", movieTitle)
                    putExtra("movie_year", movieYear)
                }
                searchMovieLauncher.launch(intent)
            }
        }

        btnAddMovie.setOnClickListener {
            val currentTitle = etMovieTitle.text.toString().trim()
            val currentYear = etMovieYear.text.toString().trim()

            viewModel.handleIntent(
                AddIntent.SelectMovie(
                    title = currentTitle,
                    year = currentYear,
                    posterUrl = viewModel.state.value?.posterUrl ?: "",
                    genre = viewModel.state.value?.genre ?: ""
                )
            )

            viewModel.handleIntent(AddIntent.AddMovieToDatabase)
        }
    }

    private fun renderState(state: com.example.wewatch.mvi.add.AddState) {
        if (etMovieTitle.text.toString() != state.title) {
            etMovieTitle.setText(state.title)
        }

        if (etMovieYear.text.toString() != state.year) {
            etMovieYear.setText(state.year)
        }

        if (state.posterUrl.isNotBlank() && state.posterUrl != "N/A") {
            Glide.with(this)
                .load(state.posterUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivSelectedPoster)
        } else {
            ivSelectedPoster.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        state.message?.let { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }

        if (state.isMovieAdded) {
            viewModel.resetMovieAddedFlag()
            finish()
        }
    }
}