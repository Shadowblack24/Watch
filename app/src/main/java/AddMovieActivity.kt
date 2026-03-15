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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieEntity
import kotlinx.coroutines.launch

class AddMovieActivity : AppCompatActivity() {

    private lateinit var etMovieTitle: EditText
    private lateinit var etMovieYear: EditText
    private lateinit var ivSelectedPoster: ImageView
    private lateinit var btnSearchMovie: Button
    private lateinit var btnAddMovie: Button

    private var selectedPosterUrl: String = ""
    private var selectedGenre: String = ""

    private val searchMovieLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedTitle = data?.getStringExtra("selected_title") ?: ""
                val selectedYear = data?.getStringExtra("selected_year") ?: ""
                selectedPosterUrl = data?.getStringExtra("selected_poster") ?: ""
                selectedGenre = data?.getStringExtra("selected_genre") ?: ""

                etMovieTitle.setText(selectedTitle)
                etMovieYear.setText(selectedYear)

                if (selectedPosterUrl.isNotBlank() && selectedPosterUrl != "N/A") {
                    Glide.with(this)
                        .load(selectedPosterUrl)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(ivSelectedPoster)
                } else {
                    ivSelectedPoster.setImageResource(android.R.drawable.ic_menu_report_image)
                }

                Toast.makeText(this, "Фильм выбран", Toast.LENGTH_SHORT).show()
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

        btnSearchMovie.setOnClickListener {
            val movieTitle = etMovieTitle.text.toString().trim()
            val movieYear = etMovieYear.text.toString().trim()

            if (movieTitle.isEmpty()) {
                Toast.makeText(this, "Введите название фильма для поиска", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SearchMovieActivity::class.java).apply {
                putExtra("movie_title", movieTitle)
                putExtra("movie_year", movieYear)
            }
            searchMovieLauncher.launch(intent)
        }

        btnAddMovie.setOnClickListener {
            val movieTitle = etMovieTitle.text.toString().trim()
            val movieYear = etMovieYear.text.toString().trim()

            if (movieTitle.isEmpty()) {
                Toast.makeText(this, "Введите название фильма", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val movie = MovieEntity(
                title = movieTitle,
                year = movieYear,
                posterUrl = selectedPosterUrl,
                genre = selectedGenre
            )

            lifecycleScope.launch {
                AppDatabase.getDatabase(this@AddMovieActivity)
                    .movieDao()
                    .insertMovie(movie)

                runOnUiThread {
                    Toast.makeText(
                        this@AddMovieActivity,
                        "Фильм добавлен",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}