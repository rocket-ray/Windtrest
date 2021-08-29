package com.example.app_team35.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_team35.R
import com.example.app_team35.models.favourites.WeatherModel

public class SevendaysAdapter (val list: List<WeatherModel>) :
    RecyclerView.Adapter<SevendaysAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView

        val weatherSymbol = view.findViewById<ImageView>(R.id.weather_symbol)
        val temperature = view.findViewById<TextView>(R.id.temperature)
        val day = view.findViewById<TextView>(R.id.weekday)
        val windstrength = view.findViewById<TextView>(R.id.wind_strength)
        val winddirection = view.findViewById<TextView>(R.id.wind_direction)
        val windchill = view.findViewById<TextView>(R.id.wind_chill)
        val humidity = view.findViewById<TextView>(R.id.humidity)
        val precipitation = view.findViewById<TextView>(R.id.precipitation)

        init {
            card = view.findViewById<CardView>(R.id.weather_card)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.weather_cardview, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dayOfWeek = list[position]

        viewHolder.weatherSymbol.setImageIcon(dayOfWeek.weatherSymbol)
        viewHolder.temperature.text = dayOfWeek.temperature
        viewHolder.day.text = dayOfWeek.day
        viewHolder.windstrength.text = dayOfWeek.windstrength
        viewHolder.winddirection.text = dayOfWeek.winddirection
        viewHolder.windchill.text = dayOfWeek.windchill
        viewHolder.humidity.text = dayOfWeek.humidity
        viewHolder.precipitation.text = dayOfWeek.precipitation
    }

    override fun getItemCount() = list.size
}
