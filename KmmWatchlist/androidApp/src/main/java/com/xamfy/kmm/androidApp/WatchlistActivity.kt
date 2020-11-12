package com.xamfy.kmm.androidApp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xamfy.kmm.shared.WatchlistSDK
import com.xamfy.kmm.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WatchlistActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    private lateinit var watchlistsRecyclerView: RecyclerView

    private val sdk = WatchlistSDK(DatabaseDriverFactory(this))

    private val watchlistsRvAdapter = WatchlistsRvAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Watchlists"
        setContentView(R.layout.activity_watchlist)

        watchlistsRecyclerView = findViewById(R.id.watchlistRv)

        watchlistsRecyclerView.adapter = watchlistsRvAdapter
        watchlistsRecyclerView.layoutManager = LinearLayoutManager(this)

        displayWatchlists()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    private fun displayWatchlists() {
        mainScope.launch {
            kotlin.runCatching {
                sdk.getWatchlists()
            }.onSuccess {
                watchlistsRvAdapter.watchlists = it
                watchlistsRvAdapter.notifyDataSetChanged()
            }.onFailure {
                Toast.makeText(this@WatchlistActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}