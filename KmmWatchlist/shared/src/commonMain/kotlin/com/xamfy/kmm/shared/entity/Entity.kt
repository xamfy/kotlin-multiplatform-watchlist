package com.xamfy.kmm.shared.entity

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunch(
    @SerialName("flight_number")
    val flightNumber: Int,
    @SerialName("mission_name")
    val missionName: String,
    @SerialName("launch_year")
    val launchYear: Int,
    @SerialName("launch_date_utc")
    val launchDateUTC: String,
    @SerialName("rocket")
    val rocket: Rocket,
    @SerialName("details")
    val details: String?,
    @SerialName("launch_success")
    val launchSuccess: Boolean?,
    @SerialName("links")
    val links: Links
)

@Serializable
data class Rocket(
    @SerialName("rocket_id")
    val id: String,
    @SerialName("rocket_name")
    val name: String,
    @SerialName("rocket_type")
    val type: String
)

@Serializable
data class Links(
    @SerialName("mission_patch")
    val missionPatchUrl: String?,
    @SerialName("article_link")
    val articleUrl: String?
)

@Serializable
data class Movie(
    @SerialName("_id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("genre")
    val genre: String,
    @SerialName("imageUrl")
    val imageUrl: String
)

@Serializable
data class Watchlist(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("movies")
    val movies: List<String>,
)

@Serializable
data class WatchlistDetail(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("movies")
    val movies: List<Movie>,
)

@Serializable
data class WatchlistResponse(
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("message")
    val message: String,
)

@Serializable
data class MovieRequest(
    @SerialName("watchlistId")
    val watchlistId: String,
    @SerialName("movieId")
    val movieId: String,
)