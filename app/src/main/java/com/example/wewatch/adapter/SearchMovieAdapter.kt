package com.example.wewatch.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.R
import com.example.wewatch.network.OmdbMovieDetailsDto

class SearchMovieAdapter(
    private var movies: List<OmdbMovieDetailsDto>,
    private val onItemSelect: (OmdbMovieDetailsDto) -> Unit
) : RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder>() {

    class SearchMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_search, parent, false)
        return SearchMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.tvTitle.text = movie.title
        holder.tvYear.text = movie.year
        holder.tvGenre.text = if (movie.genre.isBlank()) "Genre: not specified" else movie.genre

        if (movie.poster.isNotBlank() && movie.poster != "N/A") {
            Glide.with(holder.itemView.context)
                .load(movie.poster)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.ivPoster)
        } else {
            holder.ivPoster.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        // Обычный клик
        holder.itemView.setOnClickListener {
            onItemSelect(movie)
        }

        // Долгое нажатие — как мобильный аналог правой кнопки
        holder.itemView.setOnLongClickListener {
            onItemSelect(movie)
            true
        }

        // Настоящая правая кнопка мыши
        holder.itemView.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_BUTTON_PRESS &&
                event.buttonState == MotionEvent.BUTTON_SECONDARY
            ) {
                onItemSelect(movie)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<OmdbMovieDetailsDto>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}