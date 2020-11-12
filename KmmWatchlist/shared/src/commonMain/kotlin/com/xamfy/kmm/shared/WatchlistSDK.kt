package com.xamfy.kmm.shared

import com.xamfy.kmm.shared.cache.Database
import com.xamfy.kmm.shared.cache.DatabaseDriverFactory
import com.xamfy.kmm.shared.entity.*
import com.xamfy.kmm.shared.network.WatchlistApi
import io.ktor.http.*

class WatchlistSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = WatchlistApi()

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }

    @Throws(Exception::class)
    suspend fun getMovies(forceReload: Boolean): List<Movie> {
        val cachedMovies = database.getAllMovies()
        return if (cachedMovies.isNotEmpty() && !forceReload) {
            cachedMovies
        } else {
            api.getAllMovies().also {
                database.clearDatabase()
                database.createMovies(it)
            }
        }
    }

    @Throws(Exception::class)
    suspend fun getWatchlists(): List<Watchlist> {
        return api.getAllWatchlists()
    }

    @Throws(Exception::class)
    suspend fun getMoviesInWatchlist(watchlistId: String): WatchlistDetail {
        return api.getMoviesInWatchlist(watchlistId)
    }

    @Throws(Exception::class)
    suspend fun deleteMovieFromWatchlist(watchlistId: String, movieId: String): WatchlistResponse {
        return api.deleteMovieFromWatchlist(watchlistId, movieId)
    }
}
