package com.example.app_team35.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.app_team35.R
import com.example.app_team35.models.forecast.Data

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherTable.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherTable : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_table, container, false)
    }

    /**
     * Update weather data visually
     */
    fun setData(today_data: Data) {
        val today = today_data.instant.details

        // Try find available forecast data. Not all responses return all forecasts.
        var symbol_code: String? = null

        if (today_data.next1Hours != null) {
            symbol_code = today_data.next1Hours?.summary?.symbolCode
        } else if (today_data.next6Hours != null) {
            symbol_code = today_data.next6Hours?.summary?.symbolCode
        } else if (today_data.next12Hours != null) {
            symbol_code = today_data.next12Hours?.summary?.symbolCode
        }

        // Note that precipitation is never available in the next12Hours slot
        var precipitationAmout: Double? = null
        if (today_data.next1Hours != null && today_data.next1Hours?.details?.precipitationAmount !=null) {
            precipitationAmout = today_data.next1Hours?.details?.precipitationAmount
        } else if (today_data.next6Hours != null && today_data.next1Hours?.details?.precipitationAmount !=null) {
            symbol_code = today_data.next6Hours?.summary?.symbolCode
            precipitationAmout = today_data.next6Hours?.details?.precipitationAmount
        }

        if (precipitationAmout == null) {
            precipitationAmout = 0.0
        }

        if (symbol_code != null) {
            var weatherImage: ImageView = requireView().findViewById(R.id.big_weather_symbol)
            val weatherIcon = getCorrectWeatherIcon(requireContext(), symbol_code!!)
            weatherImage.setImageDrawable(weatherIcon)
        }

        val temp = getView()?.findViewById<TextView>(R.id.temperature)
        temp?.setText(today.airTemperature.toString() + " °C", TextView.BufferType.NORMAL)

        val windStrength = getView()?.findViewById<TextView>(R.id.wind_strength)
        val windStrengthVal = resources.getString(R.string.vindstyrke) + " " + (today.windSpeed ?: 0.0).toString() + " m/s"
        windStrength?.setText(windStrengthVal, TextView.BufferType.NORMAL)

        val windGust = getView()?.findViewById<TextView>(R.id.wind_gust_speed)
        val windGustVal = resources.getString(R.string.vindkast) + " " + (today.windSpeedOfGust ?: 0.0).toString() + " m/s"
        windGust?.setText(windGustVal, TextView.BufferType.NORMAL)

        val windDirection = getView()?.findViewById<TextView>(R.id.wind_direction)
        val windDirectionVal = resources.getString(R.string.vindretning) + " " + toCardinalDirection(today.windFromDirection ?: 0.0, false)
        windDirection?.setText(windDirectionVal, TextView.BufferType.NORMAL)

        val humidity = getView()?.findViewById<TextView>(R.id.humidity)
        val humidityVal = resources.getString(R.string.luftfuktighet) + " " + (today.relativeHumidity ?: 0.0).toString() + " %"
        humidity?.setText(humidityVal, TextView.BufferType.NORMAL)

        val precipitation = getView()?.findViewById<TextView>(R.id.precipitation)
        //val precipitationVal = resources.getString(R.string.nedbør) + " " + precipitationAmout!! + " mm"
        val precipitationVal = resources.getString(R.string.nedbør) + " " + (precipitationAmout ?: 0.0).toString() + " mm"
        precipitation?.setText(precipitationVal, TextView.BufferType.NORMAL)

    }
}