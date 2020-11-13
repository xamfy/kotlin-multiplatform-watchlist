package com.xamfy.kmm.shared.network

import com.xamfy.kmm.BuildKonfig
import com.xamfy.kmm.shared.entity.*
import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

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

    suspend fun getMoviesInWatchlist(watchlistId: String): WatchlistDetail {
        return httpClient.get("$WATCHLIST_ENDPOINT/$watchlistId")
    }

    suspend fun addMovieToWatchlist(watchlistId: String, movieId: String): WatchlistResponse {
        val response = httpClient.post<WatchlistResponse> {
            url("$WATCHLIST_ENDPOINT/add")
            contentType(ContentType.Application.Json)
            body = MovieRequest(watchlistId, movieId)
        }
//        println("CLIENT: Message from the server: $message")
        return response
    }

    suspend fun deleteMovieFromWatchlist(watchlistId: String, movieId: String): WatchlistResponse {
        val response = httpClient.delete<WatchlistResponse> {
            url("$WATCHLIST_ENDPOINT/delete")
            contentType(ContentType.Application.Json)
            body = MovieRequest(watchlistId, movieId)
        }
//        println("CLIENT: Message from the server: $message")
        return response
    }

    companion object {
        private const val LAUNCHES_ENDPOINT = "https://api.spacexdata.com/v3/launches"
        private val BASE_URL = BuildKonfig.baseURL
        private val MOVIES_ENDPOINT = "${BASE_URL}/movies"
        private val WATCHLIST_ENDPOINT = "${BASE_URL}/watchlist"
    }
}
