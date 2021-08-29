package com.example.app_team35.models.forecast

import android.util.Log
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.text.ParseException

/**
 * Domain object to hold the weather data we're interested in
 * Includes the name if available
 */
data class LocationForecast (
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val response: LocationForecastResponse,
)

/**
 * Get time series data for the given day relative to current date.
 * If there are multiple entries on a day, the one closest to preferredHours24 is returned
 * If data is missing for a given day, null is returned.
 */
fun forecastNdaysFromNow(forecast: LocationForecast, day: Int, preferredHours24: Int): Timesery? {
    var parser = ISODateTimeFormat.dateTimeParser()
    if (day < 1) {
        throw IllegalArgumentException("day must be >= 1")
    }
    val today = DateTime().withTimeAtStartOfDay()
    val actualDay = today.plusDays(day).withTimeAtStartOfDay()
    val nextDay = today.plusDays(day + 1).withTimeAtStartOfDay()

    var bestMatch: Timesery? = null
    var currentBestDistanceHours = Int.MAX_VALUE
    for (ts in forecast.response.properties.timeseries) {
        try {
            val date = parser.parseDateTime(ts.time)
            if (date.isAfter(actualDay) && date.isBefore(nextDay)) {
                var distance = Math.abs(date.hourOfDay().get() - preferredHours24)
                if (distance < currentBestDistanceHours) {
                    currentBestDistanceHours = distance
                    bestMatch = ts
                }
            }
        } catch (e: ParseException) {
            Log.d("forecastNdaysFromNow", "Could not parse forecast data: " + e.message)
            return null
        }
    }

    return bestMatch
}
