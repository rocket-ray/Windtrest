package com.example.app_team35.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.app_team35.repositories.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application){

    private val locationLiveData = LocationLiveData(application)
    fun getLLD() = locationLiveData

}