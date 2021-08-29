package com.example.app_team35.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.app_team35.R
import com.example.app_team35.models.favourites.WeatherModel
import com.example.app_team35.models.forecast.LocationForecast
import com.example.app_team35.models.forecast.forecastNdaysFromNow
import com.example.app_team35.models.sources.Location
import com.example.app_team35.repositories.FavoriteStorage
import com.example.app_team35.viewmodels.InterestsViewModel
import com.example.app_team35.viewmodels.LocationViewModel
import com.example.app_team35.viewmodels.MainActivityViewModel
import com.google.android.gms.maps.model.LatLng

/**
 * Weather forecast on the main page
 */
class WeatherForcastFragment : Fragment(R.layout.fragment_weather_forcast) {
    lateinit var weatherImage: ImageView
    private lateinit var viewModel: MainActivityViewModel
    private var lastForecast: LocationForecast? = null
    private var forecastList: MutableList<LocationForecast> = mutableListOf()
    private var locationList: MutableList<Location> = mutableListOf()
    private var forecastFragments: MutableList<WeatherTable> = mutableListOf()
    private var selectedActivityIndex: Int = -1
    private lateinit var favoriteStorage: FavoriteStorage
    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    lateinit var locationViewModel: LocationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        favoriteStorage = FavoriteStorage(requireContext(), 3)
        //val cardView = getView()?.findViewById<CardView>(R.id.cardView)

        val spinner: Spinner = requireView().findViewById(R.id.spinner)

        // preparing location updates
        prepRequestLocationUpdates()

        // Update activity text
        spinner.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedActivityIndex = position

                updateConditions()
            }
        }

        // Save current location
        val save_favorite_btn: ImageView = requireView().findViewById(R.id.forecast_favorite)
        save_favorite_btn.setOnClickListener {
            Log.d("Source", "Saving favorite")
            if (lastForecast != null) {
                favoriteStorage.add(
                    Location(
                        lastForecast!!.latitude,
                        lastForecast!!.longitude,
                        lastForecast!!.name
                    )
                )

                Toast.makeText(activity, "Lagret som favoritt", Toast.LENGTH_SHORT).show()
            }
        }

        getContext()?.let {
            ArrayAdapter.createFromResource(
                it, R.array.activities_array, R.layout.spinner_layout_dropdown
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown)
                spinner.adapter = adapter
            }
        }

        // Forecast table child fragments
        forecastFragments.add(childFragmentManager.findFragmentById(R.id.fragment_forecast_0) as WeatherTable)
        forecastFragments.add(childFragmentManager.findFragmentById(R.id.fragment_forecast_1) as WeatherTable)
        forecastFragments.add(childFragmentManager.findFragmentById(R.id.fragment_forecast_2) as WeatherTable)

        val search = getView()?.findViewById<SearchView>(R.id.forecast_search)
        search?.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                var q = "*".plus(query?.toUpperCase()).plus("*")
                search.onActionViewCollapsed()
                if (query!= null){

                    Log.d("Source", "Location search:" + q)

                    viewModel.loadSources(q)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                //result?.setText(newText)
                return true
            }
        })

        val location = getView()?.findViewById<ImageView>(R.id.forecast_location)
        location?.setOnClickListener{
            requestLocationUpdates()
        }


        // Search result
        viewModel.getSourceQueryLiveData()?.observe(viewLifecycleOwner, Observer {
            // TODO: not needed
            locationList.clear()
            if (it.isNotEmpty()){
                viewModel.loadForecast(it.first().latitude, it.first().longitude, it.first().name)
            }else{
                //Toast.makeText(activity?.applicationContext!!, "Did not find a match to your location search", Toast.LENGTH_LONG).show()
                Log.d("Source", "Location search did not return any hits")
            }
        })

        // Forecast result
        viewModel.getForecastLiveData()?.observe(viewLifecycleOwner, Observer {it: LocationForecast ->
            // Update through adapter. Since we only show one item at a time for the selected marker,
            // we clear the list and then add the LocationForecast object.
            forecastList.clear()
            forecastList.add(it)

            this.lastForecast = it

            val textLocation = getView()?.findViewById<TextView>(R.id.text_location_name)
            //textLocation!!.setText(it.name.toUpperCase().replaceFirst(" - ", ", "), TextView.BufferType.NORMAL)
            textLocation!!.setText(formatLocationName(it.name), TextView.BufferType.NORMAL)

            // CardView doesn't have adapters, update manually
            val today_data = it.response.properties.timeseries[0].data
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
                weatherImage = requireView().findViewById(R.id.big_weather_symbol)
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
            val precipitationVal = resources.getString(R.string.nedbør) + " " + (precipitationAmout ?: 0.0).toString() + " mm"
            precipitation?.setText(precipitationVal, TextView.BufferType.NORMAL)

            repeat(3) { index ->
                val forecast = forecastNdaysFromNow(it, index + 1, 12)
                if (forecast != null) {
                    forecastFragments[index].setData(forecast!!.data)
                }
            }

            updateConditions()
        })
   }

    fun updateConditions() {
        if (this.selectedActivityIndex >= 0 && lastForecast != null) {
            val today_data = lastForecast!!.response.properties.timeseries[0].data
            val today = today_data.instant.details

            val spinner: Spinner = requireView().findViewById(R.id.spinner)
            val conditions = InterestsViewModel().checkConditions(
                spinner.getItemAtPosition(this.selectedActivityIndex).toString(), today.airTemperature ?: 0.0, today.windSpeed ?: 0.0
            )
            val conditionView = getView()?.findViewById<TextView>(R.id.activity_conditions)
            conditionView!!.setText(conditions, TextView.BufferType.NORMAL)
        }
    }

    private fun prepRequestLocationUpdates() {
        if(ContextCompat.checkSelfPermission(activity?.applicationContext!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocationUpdates()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun requestLocationUpdates() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLLD().observe(viewLifecycleOwner, Observer {
            val latiLong = LatLng(it.latitude, it.longitude)
            viewModel.loadForecast(latiLong.latitude, latiLong.longitude, "Din posisjon")
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestLocationUpdates()
                } else {
                    Toast.makeText(activity?.applicationContext!!, "Please, enable location in settings, or search for location", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}