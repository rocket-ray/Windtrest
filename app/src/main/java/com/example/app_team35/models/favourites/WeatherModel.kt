package com.example.app_team35.models.favourites

import android.graphics.drawable.Icon

data class WeatherModel(
        /** Trenger kanskje ikke lagre location og date i cardviewet.
        val location: Location
        val date: Date
        */
        val weatherSymbol: Icon, // usikker på formatet - endre her og i adapter om ikke URI
        val temperature: String,
        val day: String,
        val windstrength: String,
        val winddirection: String,
        val windchill: String,
        val humidity: String,
        val precipitation: String
)
