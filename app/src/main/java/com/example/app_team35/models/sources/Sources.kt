package com.example.app_team35.models.sources

import com.squareup.moshi.Json

/**
 * Domain object to hold the source information
 * Includes Moshi mappings so it can be saved in shared preferences
 */
data class Location (
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "name")
    val name: String
)
