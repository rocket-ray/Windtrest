package com.example.app_team35.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.app_team35.models.forecast.LocationForecast
import com.example.app_team35.models.forecast.forecastNdaysFromNow
import com.example.app_team35.models.sources.Location
import com.example.app_team35.repositories.WeatherApi

/**
 * ViewModel for the main activity. This exposes forecast LiveData for the currently selected
 * map marker. The LiveData is observed from the main activity. There's also a function to request
 * a new forecast for a given location.
 *
 * The point of the ViewModel is that the UI doesn't need to know how forecast data is
 * retrieved, and the service doesn't need to know how to update the UI.
 */
class MainActivityViewModel : ViewModel() {
    private val currentTemperatureData: MutableLiveData<LocationForecast> = MutableLiveData<LocationForecast>()
    private val currentSourceLocationData: MutableLiveData<MutableList<Location>> = MutableLiveData<MutableList<Location>>()

    /**
     * Returns an observable LiveData object for the current forecast
     */
    fun getForecastLiveData(): MutableLiveData<LocationForecast>? {
        return currentTemperatureData
    }

    /**
     * Returns an observable LiveData object for the current source query
     */
    fun getSourceQueryLiveData(): MutableLiveData<MutableList<Location>>? {
        return currentSourceLocationData
    }

    /**
     * Called by the activity to load the forecast for a given map marker
     * If the location name is missing, the API will be used to find it
     */
    fun loadForecast(latitude: Double, longitude: Double, name: String?) {
        // This function uses a co-routine to asynchronously load forecast data from met.no
        // When the result is ready, the interesting data is extracted and put into
        // a LocationForecast domain object.
        viewModelScope.launch {
            try {
                // Get the name of the location
                var locationName = "";
                if (name != null) {
                    locationName = name!!
                }
                else {
                    val geometry = "nearest(POINT(" + longitude + " " + latitude + "))"
                    val nameResult = WeatherApi.frostService.getLocationNames(geometry);

                    if (nameResult.data != null && nameResult.data?.size > 0) {
                        locationName = nameResult.data[0].name!!;
                    }
                }

                val result = WeatherApi.forecastService.getForecast(latitude, longitude)
                if (result.properties.timeseries.isNotEmpty()) {

                    // This will cause observers of forecast data to be notified
                    currentTemperatureData.value = LocationForecast(
                        latitude,
                        longitude,
                        locationName,
                        result)
                }
                else {
                    Log.d("MapViewModel", "Forecast data was not available")
                }
            } catch (e: Exception) {
                //_response.value = "Failure: ${e.message}"
                Log.d("MapViewModel", "Could not load forecast data: " + e.stackTraceToString())

            }
        }
    }

    /**
     * Called by the activity to load locations, given an exact place name query
     * such as "OSLO - HOVIN", or a wildcard query such as "OSLO*" or "*SANDAKER*"
     */
    fun loadSources(nameQuery: String) {
        viewModelScope.launch {
            val sourceList = mutableListOf<Location>()
            try {
                val result = WeatherApi.frostService.getSources("SensorSystem", nameQuery)
                if (result.data.isNotEmpty()) {
                    for (source in result.data) {

                        sourceList.add(
                            Location(
                                longitude = source.geometry?.coordinates?.get(0)!!,
                                latitude = source.geometry?.coordinates?.get(1)!!,
                                name = source.name!!
                            ))
                    }
                }
                else {
                    Log.d("MapViewModel", "Source data was not available")
                }
            } catch (e: Exception) {
                //_response.value = "Failure: ${e.message}"
                Log.d("MapViewModel", "Could not load source data: " + e.message)
            } finally {
                // This will cause observers of the source list to be notified

                // If there's an error (such as a 404 Not Found response from the api), this will be an empty list
                currentSourceLocationData.value = sourceList;
            }
        }
    }
}