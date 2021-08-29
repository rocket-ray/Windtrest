package com.example.app_team35.repositories

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.app_team35.models.LocationModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Deferred

const val use_device_location = "use_device_location"

class LocationLiveData(context: Context) : LiveData<LocationModel>(){
    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    // Locationclient
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val appContext = context.applicationContext
    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    // similar to OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onInactive() {
        super.onInactive()
        // unsubscribe to location updates
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    override fun onActive() {
        super.onActive()
        if (isUsingDeviceLocation()){
            if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            startLocationUpdates()
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

    }

    // if the locationcallback have recieved a location update, it then sets the location model lat and long from device position
    fun setLocation(location: Location) {
        // setting the value of the LocationModel

        value = LocationModel(location.longitude, location.latitude)
    }


    private fun startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }else{
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

    }

    // new class that handles callbacks
    private val locationCallback = object : LocationCallback(){
        // getting a location  from the device(locationResult built-in from fusedlocationclient)
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            // if there is null locationresult
            locationResult ?: return

            // if there is a locationResult
            for (location in locationResult.locations){
                setLocation(location)
            }
        }
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(use_device_location, true)
    }



// location request object (singelton object = companion object) as it only needs to be called once
    companion object {
        val ten_min : Long = 600000
        val locationRequest : LocationRequest = LocationRequest.create().apply {
            interval = ten_min// proactively ask for location updates interval in ms
            fastestInterval = ten_min /2   //piggy back on other apps getting data
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY // need an accurate location if using it.
        }
    }


}