package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.wewatch.data.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var fabAddMovie: FloatingActionButton
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var recyclerMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "WeWatch"

        fabAddMovie = findViewById(R.id.fabAddMovie)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        recyclerMovies = findViewById(R.id.recyclerMovies)

        fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    private fun loadMovies() {
        lifecycleScope.launch {
            val movies = AppDatabase.getDatabase(this@MainActivity)
                .movieDao()
                .getAllMovies()

            runOnUiThread {
                if (movies.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                    recyclerMovies.visibility = View.GONE
                } else {
                    emptyStateLayout.visibility = View.GONE
                    recyclerMovies.visibility = View.VISIBLE

                    Toast.makeText(
                        this@MainActivity,
                        "Фильмов в базе: ${movies.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_selected -> {
                Toast.makeText(this, "Удаление отмеченных сделаем позже", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}