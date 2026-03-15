package com.example.wewatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.R
import com.example.wewatch.data.MovieEntity

class MovieAdapter(
    private var movies: MutableList<MovieEntity>,
    private val onCheckedChange: (MovieEntity, Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val checkBoxDelete: CheckBox = itemView.findViewById(R.id.checkBoxDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_main, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.tvTitle.text = movie.title
        holder.tvYear.text = if (movie.year.isBlank()) "Год не указан" else movie.year

        if (movie.posterUrl.isNotBlank() && movie.posterUrl != "N/A") {
            Glide.with(holder.itemView.context)
                .load(movie.posterUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.ivPoster)
        } else {
            holder.ivPoster.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.checkBoxDelete.setOnCheckedChangeListener(null)
        holder.checkBoxDelete.isChecked = movie.isSelectedForDelete

        holder.checkBoxDelete.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(movie, isChecked)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<MovieEntity>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}