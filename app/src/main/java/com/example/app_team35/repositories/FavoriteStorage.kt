package com.example.app_team35.repositories

import android.content.Context
import com.example.app_team35.R
import com.example.app_team35.models.sources.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * This container is used to save favorite locations to shared preferences as JSON
 */
data class FavoriteContainer (
    @Json(name = "favorites")
    var locations: MutableList<Location>
)

/**
 * Storage for favorites
 * Currently saves lat/long/name
 */
class FavoriteStorage constructor(context: Context, maxFavorites: Int) {
    var moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    var jsonAdapter: JsonAdapter<Any> = moshi.adapter<Any>(FavoriteContainer::class.java)

    val sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_favorites), Context.MODE_PRIVATE)
    val sharedPrefKey = context.getString(R.string.shared_pref_key_favorite_container);
    val maxContainerSize = maxFavorites;

    /**
     * Return all stored favorite locations
     */
    fun getAll(): MutableList<Location> {
        var favorites = FavoriteContainer(locations = mutableListOf())

        var arrayAsString = sharedPref.getString(sharedPrefKey, "");
        if (arrayAsString!!.length > 0) {
            favorites = jsonAdapter.fromJson(arrayAsString) as FavoriteContainer
        }

        return favorites.locations
    }

    /**
     * Add favorite to store
     */
    fun add(location: Location) {
        var favorites = FavoriteContainer(locations = mutableListOf())

        var arrayAsString = sharedPref.getString(sharedPrefKey, "");
        if (arrayAsString!!.length > 0) {
            favorites = jsonAdapter.fromJson(arrayAsString) as FavoriteContainer
        }

        // If location is not already saved, add it
        if (favorites.locations.count { it.name.equals(location.name) } == 0) {
            favorites.locations.add(location)
        }

        while (favorites.locations.size > maxContainerSize) {
            favorites.locations.removeAt(0);
        }

        val faveAsString = jsonAdapter.toJson(favorites)

        with (sharedPref.edit()) {
            putString(sharedPrefKey, faveAsString)
            apply()
        }
    }

    /**
     * Remove favorite from store
     */
    fun removeByName(locationName: String) {
        var favorites = FavoriteContainer(locations = mutableListOf())

        var arrayAsString = sharedPref.getString(sharedPrefKey, "");
        if (arrayAsString!!.length > 0) {
            favorites = jsonAdapter.fromJson(arrayAsString) as FavoriteContainer
        }

        val indexOfLocationToDelete = favorites.locations.indexOfFirst { it.name.equals(locationName) }
        if (indexOfLocationToDelete != -1) {
            favorites.locations.removeAt(indexOfLocationToDelete)
        }
        val faveAsString = jsonAdapter.toJson(favorites)

        with (sharedPref.edit()) {
            putString(sharedPrefKey, faveAsString)
            apply()
        }
    }

    /**
     * Remove favorite from store (searching by Location's name)
     */
    fun remove(location: Location) {
        removeByName(location.name)
    }
}
