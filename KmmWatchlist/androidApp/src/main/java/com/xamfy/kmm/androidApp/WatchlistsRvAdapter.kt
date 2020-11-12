package com.xamfy.kmm.androidApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xamfy.kmm.shared.entity.Watchlist

class WatchlistsRvAdapter(var watchlists: List<Watchlist>) :
    RecyclerView.Adapter<WatchlistsRvAdapter.WatchlistViewHolder>() {

    inner class WatchlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val watchlistNameTextView = itemView.findViewById<TextView>(R.id.watchlistNameTV)

        fun bindData(watchlist: Watchlist) {
            watchlistNameTextView.text = watchlist.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_watchlist, parent, false)
            .run(::WatchlistViewHolder)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bindData(watchlists[position])
    }

    override fun getItemCount(): Int = watchlists.count()
}