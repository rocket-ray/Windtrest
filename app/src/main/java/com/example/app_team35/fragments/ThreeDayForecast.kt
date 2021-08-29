package com.example.app_team35.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.app_team35.R
import com.example.app_team35.models.forecast.LocationForecast
import com.example.app_team35.models.forecast.forecastNdaysFromNow

/**
 * Three-day forecast for a given location
 */
class ThreeDayForecast : Fragment(R.layout.fragment_three_day_forecast) {

    private var forecastFragments: MutableList<WeatherTable> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        forecastFragments.add(childFragmentManager.findFragmentById(R.id.fragment_forecast_0) as WeatherTable)
        forecastFragments.add(childFragmentManager.findFragmentById(R.id.fragment_forecast_1) as WeatherTable)
        forecastFragments.add(childFragmentManager.findFragmentById(R.id.fragment_forecast_2) as WeatherTable)
    }

    fun setData(it: LocationForecast) {
        val heading = getView()?.findViewById<TextView>(R.id.forecast_3days_heading)
        heading!!.setText(formatLocationName(it.name), TextView.BufferType.NORMAL)

        repeat(3) { index ->
            val forecast = forecastNdaysFromNow(it, index + 1, 12)
            if (forecast != null) {
                forecastFragments[index].setData(forecast!!.data)
            }
        }
    }
}