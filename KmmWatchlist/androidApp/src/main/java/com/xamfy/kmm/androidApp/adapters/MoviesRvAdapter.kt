package com.xamfy.kmm.androidApp.adapters

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xamfy.kmm.androidApp.R
import com.xamfy.kmm.shared.entity.Movie

class MoviesRvAdapter(var movies: List<Movie>) :
    RecyclerView.Adapter<MoviesRvAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        private val movieImageView = itemView.findViewById<ImageView>(R.id.movieImage)
        private val movieTitleTextView = itemView.findViewById<TextView>(R.id.movieTitle)
        private val movieDescTextView = itemView.findViewById<TextView>(R.id.movieDesc)
        private val movieGenreTextView = itemView.findViewById<TextView>(R.id.movieGenre)
        private val optionsImageView = itemView.findViewById<ImageView>(R.id.moreVertIcon)

        init {
            optionsImageView.setOnClickListener(this)
        }

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

        override fun onClick(v: View?) {
            if (v != null) {
                showPopupMenu(v)
            }
        }

        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.list_item_popup_menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when(item?.itemId) {
                R.id.action_add_to_watchlist -> {
                    Toast.makeText(itemView.context, movieTitleTextView.text, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
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