package com.xamfy.kmm.androidApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xamfy.kmm.androidApp.adapters.WatchlistsRvAdapter
import com.xamfy.kmm.shared.WatchlistSDK
import com.xamfy.kmm.shared.cache.DatabaseDriverFactory
import com.xamfy.kmm.shared.entity.Watchlist
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WatchlistActivity : AppCompatActivity(), WatchlistsRvAdapter.OnItemListener {
    private val TAG = "WatchlistActivity"

    private val mainScope = MainScope()

    private lateinit var watchlistsRecyclerView: RecyclerView

    private val sdk = WatchlistSDK(DatabaseDriverFactory(this))

    private val watchlistsRvAdapter = WatchlistsRvAdapter(listOf(), this)

    private var watchlists = listOf<Watchlist>()

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
                watchlists = it
                watchlistsRvAdapter.notifyDataSetChanged()
            }.onFailure {
                Toast.makeText(this@WatchlistActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onItemClick(position: Int) {
        Log.i(TAG, "onItemClick: clicked")
        val watchlist = watchlists[position]
        val intent = Intent(this@WatchlistActivity, WatchlistDetailActivity::class.java)
        intent.putExtra("watchlistId", watchlist.id)
        intent.putExtra("watchlistName", watchlist.name)
        startActivity(intent)
    }
}