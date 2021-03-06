package com.xamfy.kmm.androidApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xamfy.kmm.androidApp.adapters.LaunchesRvAdapter
import com.xamfy.kmm.androidApp.adapters.MoviesRvAdapter
import com.xamfy.kmm.shared.Greeting
import com.xamfy.kmm.shared.WatchlistSDK
import com.xamfy.kmm.shared.cache.DatabaseDriverFactory
import com.xamfy.kmm.shared.entity.Movie
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity(), MoviesRvAdapter.OnPopupMenuItemListener {
    private val TAG = "MainActivity"

    private val mainScope = MainScope()

    //    private lateinit var launchesRecyclerView: RecyclerView
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val sdk = WatchlistSDK(DatabaseDriverFactory(this))

    private val launchesRvAdapter = LaunchesRvAdapter(listOf())
    private val moviesRvAdapter = MoviesRvAdapter(listOf(), this)
    private var movies = listOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Movies"
        setContentView(R.layout.activity_main)

//        launchesRecyclerView = findViewById(R.id.launchesListRv)
        moviesRecyclerView = findViewById(R.id.moviesListRv)

        progressBarView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeContainer)

//        launchesRecyclerView.adapter = launchesRvAdapter
//        launchesRecyclerView.layoutManager = LinearLayoutManager(this)
        moviesRecyclerView.adapter = moviesRvAdapter
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
//            displayLaunches(true)
            displayMovies(true)
        }

//        displayLaunches(false)
        displayMovies(false)

//        val tv: TextView = findViewById(R.id.text_view)
//        tv.text = greet()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

//    private fun displayLaunches(needReload: Boolean) {
//        progressBarView.isVisible = true
//        mainScope.launch {
//            kotlin.runCatching {
//                sdk.getLaunches(needReload)
//            }.onSuccess {
//                launchesRvAdapter.launches = it
//                launchesRvAdapter.notifyDataSetChanged()
//            }.onFailure {
//                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//            progressBarView.isVisible = false
//        }
//    }

    private fun displayMovies(needReload: Boolean) {
        progressBarView.isVisible = true
        mainScope.launch {
            kotlin.runCatching {
                sdk.getMovies(needReload)
            }.onSuccess {
                moviesRvAdapter.movies = it
                movies = it
                moviesRvAdapter.notifyDataSetChanged()
            }.onFailure {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            progressBarView.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.open_watchlists -> {
                startActivity(Intent(this@MainActivity, WatchlistActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPopupMenuItemClick(position: Int) {
        val movieId = movies[position].id
//        Toast.makeText(this@MainActivity, movieId, Toast.LENGTH_SHORT).show()

        mainScope.launch {
            kotlin.runCatching {
                sdk.getWatchlists()
            }.onSuccess {
                // show watchlists in a dialog or activity
                val namesWatchlist = mutableListOf<String>()
                it.forEach { watchlist ->
                    namesWatchlist.add(watchlist.name)
                }

                var chosen = -1

                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(resources.getString(R.string.choose_watchlist))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                        // Respond to positive button press
//                        Toast.makeText(this@MainActivity, "$which", Toast.LENGTH_SHORT).show()
                        if (chosen != -1) {
                            val watchlistId = it[chosen].id
                            addMovieToWatchlist(watchlistId, movieId)
                        }
                    }
                    // Single-choice items (initialized with checked item)
                    .setSingleChoiceItems(namesWatchlist.toTypedArray(), -1) { dialog, which ->
                        // Respond to item chosen
                        chosen = which
//                        Toast.makeText(this@MainActivity, "$which", Toast.LENGTH_SHORT).show()
                    }
                    .show()
//                    .setItems(namesWatchlist.toTypedArray()) { dialog, which ->
//                        // Respond to item chosen
//                    }
//                    .show()
            }.onFailure {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    fun addMovieToWatchlist(watchlistId: String, movieId: String) {
        mainScope.launch {
            kotlin.runCatching {
                sdk.addMovieToWatchlist(watchlistId, movieId)
            }.onSuccess {
                Toast.makeText(this@MainActivity, "Movie added to watchlist", Toast.LENGTH_SHORT)
                    .show()
            }.onFailure {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.i(TAG, "onPopupMenuItemClick: ${it.localizedMessage}")
            }
        }
    }

}
