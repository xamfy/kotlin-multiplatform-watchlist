package com.xamfy.kmm.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xamfy.kmm.androidApp.adapters.WatchlistMoviesRvAdapter
import com.xamfy.kmm.shared.WatchlistSDK
import com.xamfy.kmm.shared.cache.DatabaseDriverFactory
import com.xamfy.kmm.shared.entity.Movie
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WatchlistDetailActivity : AppCompatActivity(), WatchlistMoviesRvAdapter.OnPopupMenuItemListener {
    private val TAG = "WatchlistDetailActivity"

    private val mainScope = MainScope()

    private lateinit var moviesRecyclerView: RecyclerView
    private val moviesRvAdapter = WatchlistMoviesRvAdapter(listOf(), this)
    private var movies = mutableListOf<Movie>()

    private val sdk = WatchlistSDK(DatabaseDriverFactory(this))

    private lateinit var watchlistId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        val watchlistName = extras?.get("watchlistName") as String
        watchlistId = extras.get("watchlistId") as String

        title = watchlistName

        setContentView(R.layout.activity_watchlist_detail)

        moviesRecyclerView = findViewById(R.id.watchlistDetailRV)
        moviesRecyclerView.adapter = moviesRvAdapter
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        displayMovies(watchlistId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    private fun displayMovies(watchlistId: String) {
        mainScope.launch {
            kotlin.runCatching {
                sdk.getMoviesInWatchlist(watchlistId)
            }.onSuccess {
                moviesRvAdapter.movies = it.movies
                movies = it.movies as MutableList<Movie>
                moviesRvAdapter.notifyDataSetChanged()
            }.onFailure {
                Toast.makeText(this@WatchlistDetailActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPopupMenuItemClick(position: Int) {
        val movieId = movies[position].id
//        Toast.makeText(this@WatchlistDetailActivity, movieId, Toast.LENGTH_SHORT).show()
        // remove movieId from watchlistId watchlist
        mainScope.launch {
            kotlin.runCatching {
                sdk.deleteMovieFromWatchlist(watchlistId, movieId)
            }.onSuccess {
                Log.i(TAG, "onPopupMenuItemClick: $it")
                movies.removeAt(position)
                moviesRecyclerView.adapter?.notifyItemRemoved(position)
                moviesRecyclerView.adapter?.notifyItemRangeChanged(position, movies.size)
            }.onFailure {
                Toast.makeText(this@WatchlistDetailActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "onPopupMenuItemClick: ${it.localizedMessage}")
            }
        }
    }
}