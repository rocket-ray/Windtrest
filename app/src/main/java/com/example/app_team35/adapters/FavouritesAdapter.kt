package com.example.app_team35.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_team35.R
import com.example.app_team35.models.favourites.WeatherModel
import com.example.app_team35.models.sources.Location

class FavouritesAdapter (val list: MutableList<Location>) :
    RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView

        val place = view.findViewById<TextView>(R.id.place_name)

        val weatherRecycler = view.findViewById<RecyclerView>(R.id.weather_recycler)


        init {
            card = view.findViewById<CardView>(R.id.favourite_card)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.favourite_cardview, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val location = list[position]
        viewHolder.place.text = location.name

    }

    override fun getItemCount() = list.size

}

