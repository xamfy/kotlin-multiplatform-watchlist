package com.xamfy.kmm.androidApp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xamfy.kmm.androidApp.R
import com.xamfy.kmm.shared.entity.Movie

class MoviesRvAdapter(var movies: List<Movie>) :
    RecyclerView.Adapter<MoviesRvAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImageView = itemView.findViewById<ImageView>(R.id.movieImage)
        private val movieTitleTextView = itemView.findViewById<TextView>(R.id.movieTitle)
        private val movieDescTextView = itemView.findViewById<TextView>(R.id.movieDesc)
        private val movieGenreTextView = itemView.findViewById<TextView>(R.id.movieGenre)

        fun bindData(movie: Movie) {
            movieImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            movieImageView.load(movie.imageUrl) {
//                crossfade(750)
//                scale(Scale.FILL)
            }
            movieTitleTextView.text = movie.title
            movieDescTextView.text = movie.description
            movieGenreTextView.text = movie.genre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
            .run(::MovieViewHolder)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindData(movies[position])
    }

    override fun getItemCount(): Int = movies.count()
}