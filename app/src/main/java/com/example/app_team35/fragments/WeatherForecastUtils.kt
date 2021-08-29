package com.example.app_team35.fragments

import android.content.res.Resources
import android.graphics.drawable.Drawable
import kotlin.math.floor

/**
 * Convert from met.no API's degrees to cardinal direction
 * Input is normalized
 */
fun toCardinalDirection(degree: Double, arrow_only: Boolean): String {
    var bearing = degree
    if (bearing < 0 && bearing > -180) {
        bearing = 360.0 + bearing
    }
    if (bearing > 360 || bearing < -180) {
        return "-"
    }
    val directions_text = arrayOf(
        "N", "NNØ", "NØ", "ØNØ", "Ø", "ØSØ", "SØ", "SSØ",
        "S", "SSV", "SV", "VSV", "V", "VNV", "NV", "NNV",
        "N"
    )

    val directions = arrayOf(
        "↓", "↙", "↙", "↙", "←", "↖", "↖", "↖",
        "↑", "↗", "↗", "↗", "→", "↘", "↘", "↘",
        "↓"
    )

    val index = floor((bearing + 11.25) % 360 / 22.5).toInt()
    var res = directions[index]
    if (!arrow_only) {
        res += " " + directions_text[index]
    }
    return res
}

/**
 * Give a symbol code, return the corresponding image
 */
fun getCorrectWeatherIcon(context: android.content.Context, symbol_code: String): Drawable {
    // Log.d("Source", "Loading image:" + "yr_weathericons/" + symbol_code + ".png")
    val resources: Resources = context.resources!!
    val resourceId: Int = resources.getIdentifier(
        symbol_code, "drawable",
        context?.packageName
    )
    return resources.getDrawable(resourceId, null)
}

/**
 * Return the display version of the API location name
 */
fun formatLocationName(name: String): String {
    var res = name.toUpperCase();
    var idx = res.indexOf('-');
    if (idx > 0) {
        res = name.substring(0, idx);
    }

    return res; //name.toUpperCase().replaceFirst(" - ", ", ")
}