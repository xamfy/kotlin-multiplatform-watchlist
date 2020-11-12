package com.xamfy.kmm.shared.network

import com.xamfy.kmm.shared.entity.Movie
import com.xamfy.kmm.shared.entity.RocketLaunch
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

import com.xamfy.kmm.BuildKonfig
import com.xamfy.kmm.shared.entity.Watchlist
import io.ktor.http.*

class WatchlistApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun getAllLaunches(): List<RocketLaunch> {
        return httpClient.get(LAUNCHES_ENDPOINT)
    }

    suspend fun getAllMovies(): List<Movie> {
        return httpClient.get(MOVIES_ENDPOINT)
    }

    suspend fun getAllWatchlists(): List<Watchlist> {
        return httpClient.get(WATCHLIST_ENDPOINT)
    }

    suspend fun getMoviesInWatchlist(watchlistId: String): List<Movie> {
        return httpClient.get("$WATCHLIST_ENDPOINT/$watchlistId")
    }

    suspend fun addMovieToWatchlist(watchlistId: String, movieId: String) {
        val message = httpClient.post<String> {
            url("$WATCHLIST_ENDPOINT/add")
            contentType(ContentType.Application.Json)
            body = AddMovie(watchlistId, movieId)
        }
        println("CLIENT: Message from the server: $message")
    }

    companion object {
        private const val LAUNCHES_ENDPOINT = "https://api.spacexdata.com/v3/launches"
        private val BASE_URL = BuildKonfig.baseURL
        private val MOVIES_ENDPOINT = "${BASE_URL}/movies"
        private val WATCHLIST_ENDPOINT = "${BASE_URL}/watchlist"
    }
}

data class AddMovie(val watchlistId: String, val movieId: String)
