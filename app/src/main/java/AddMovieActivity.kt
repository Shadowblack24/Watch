package com.example.wewatch

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wewatch.data.AppDatabase
import com.example.wewatch.data.MovieEntity
import kotlinx.coroutines.launch

class AddMovieActivity : AppCompatActivity() {

    private lateinit var etMovieTitle: EditText
    private lateinit var etMovieYear: EditText
    private lateinit var btnSearchMovie: Button
    private lateinit var btnAddMovie: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        title = "Add Movie"

        etMovieTitle = findViewById(R.id.etMovieTitle)
        etMovieYear = findViewById(R.id.etMovieYear)
        btnSearchMovie = findViewById(R.id.btnSearchMovie)
        btnAddMovie = findViewById(R.id.btnAddMovie)

        btnSearchMovie.setOnClickListener {
            Toast.makeText(this, "Поиск подключим следующим этапом", Toast.LENGTH_SHORT).show()
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
                year = movieYear
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