package com.xamfy.kmm.androidApp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xamfy.kmm.androidApp.R
import com.xamfy.kmm.shared.entity.Watchlist

class WatchlistsRvAdapter(
    var watchlists: List<Watchlist>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<WatchlistsRvAdapter.WatchlistViewHolder>() {
    private val TAG = "WatchlistsRvAdapter"

    inner class WatchlistViewHolder(itemView: View, private val onItemClickListener:
    OnItemListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val watchlistNameTextView = itemView.findViewById<TextView>(R.id.watchlistNameTV)

        fun bindData(watchlist: Watchlist) {
            watchlistNameTextView.text = watchlist.name
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.i(TAG, "onClick: clicked")
            onItemClickListener.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
//        return LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_watchlist, parent, false)
//            .run(::WatchlistViewHolder(onItemListener))
        val view =  LayoutInflater.from(parent.context)
            .inflate(R.layout.item_watchlist, parent, false)
        return WatchlistViewHolder(view, onItemListener)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bindData(watchlists[position])
    }

    override fun getItemCount(): Int = watchlists.count()

    interface OnItemListener {
        fun onItemClick(position: Int)
    }
}