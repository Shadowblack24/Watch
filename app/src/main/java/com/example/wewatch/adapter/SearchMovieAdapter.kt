package com.example.wewatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.R
import com.example.wewatch.network.OmdbMovieDto

class SearchMovieAdapter(
    private var movies: List<OmdbMovieDto>,
    private val onItemClick: (OmdbMovieDto) -> Unit
) : RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder>() {

    class SearchMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
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

        if (movie.poster.isNotBlank() && movie.poster != "N/A") {
            Glide.with(holder.itemView.context)
                .load(movie.poster)
                .into(holder.ivPoster)
        } else {
            holder.ivPoster.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener {
            onItemClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<OmdbMovieDto>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}