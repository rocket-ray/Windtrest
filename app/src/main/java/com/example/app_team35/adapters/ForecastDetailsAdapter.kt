package com.example.app_team35.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_team35.R
import com.example.app_team35.models.forecast.LocationForecast

/**
 * This adapter is used to update the current forecast in the details pane,
 * which is displayed below the map when a map marker is clicked.
 */
class ForecastDetailsAdapter(list : MutableList<LocationForecast>) :
RecyclerView.Adapter<ForecastDetailsAdapter.ViewHolder>() {
    private var temperatureList: MutableList<LocationForecast> = mutableListOf()

    init {
        this.temperatureList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_details_layout, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val title : TextView = view.findViewById(R.id.temperature_celsius)
        val wind : TextView = view.findViewById(R.id.wind)
        val windgust : TextView = view.findViewById(R.id.windgust)
    }

    override fun getItemCount(): Int {
        return temperatureList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var forecastToday = temperatureList[position].response.properties.timeseries[0].data.instant.details;
        holder.title.text = "Temperatur: " + forecastToday.airTemperature + " °C"
        holder.wind.text = "Vindstyrke: " + (forecastToday.windSpeed ?: 0.0) + " m/s"
        holder.windgust.text = "Vindkast: " + (forecastToday.windSpeedOfGust?: 0.0) + " m/s"
    }
}
