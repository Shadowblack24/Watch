package com.example.wewatch

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddMovieActivity : AppCompatActivity() {

    private lateinit var btnSearchMovie: Button
    private lateinit var btnAddMovie: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        title = "Add Movie"

        btnSearchMovie = findViewById(R.id.btnSearchMovie)
        btnAddMovie = findViewById(R.id.btnAddMovie)

        btnSearchMovie.setOnClickListener {
            Toast.makeText(this, "Позже здесь откроется экран поиска фильмов", Toast.LENGTH_SHORT).show()
        }

        btnAddMovie.setOnClickListener {
            Toast.makeText(this, "Позже здесь будет добавление фильма в базу", Toast.LENGTH_SHORT).show()
        }
    }
}