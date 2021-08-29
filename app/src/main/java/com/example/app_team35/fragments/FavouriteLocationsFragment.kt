package com.example.app_team35.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.app_team35.R
import com.example.app_team35.models.forecast.LocationForecast
import com.example.app_team35.models.forecast.forecastNdaysFromNow
import com.example.app_team35.repositories.FavoriteStorage
import com.example.app_team35.viewmodels.MainActivityViewModel

/**
 * The main favorite screen
 */
class FavouriteLocationsFragment : Fragment(R.layout.fragment_favourite_locations) {

    // A list of fragments, each displaying three days worth of forecast
    private var threedayFragments: MutableList<ThreeDayForecast> = mutableListOf()
    private lateinit var favoriteStorage: FavoriteStorage
    private lateinit var viewModel: MainActivityViewModel
    private var nextAvailableFragmentIndex: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        favoriteStorage = FavoriteStorage(requireContext(), 3)
        nextAvailableFragmentIndex = 0

        threedayFragments.add(childFragmentManager.findFragmentById(R.id.fragment0) as ThreeDayForecast)
        threedayFragments.add(childFragmentManager.findFragmentById(R.id.fragment1) as ThreeDayForecast)
        threedayFragments.add(childFragmentManager.findFragmentById(R.id.fragment2) as ThreeDayForecast)


        for (fragment in threedayFragments) {
            fragment.view?.visibility = View.INVISIBLE
        }

        // Load forecast data for all favorites
        for (it in favoriteStorage.getAll()) {
            viewModel.loadForecast(it.latitude, it.longitude, it.name)
        }

        // Observe forecast data. This will be called once per favorite.
        viewModel.getForecastLiveData()?.observe(viewLifecycleOwner, Observer {
            val fragment = threedayFragments[nextAvailableFragmentIndex]
            nextAvailableFragmentIndex++
            fragment.setData(it)
            fragment.view?.visibility = View.VISIBLE
        })
    }
}