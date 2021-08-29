package com.example.app_team35.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_team35.R
import com.example.app_team35.adapters.ForecastDetailsAdapter
import com.example.app_team35.models.forecast.LocationForecast
import com.example.app_team35.models.forecast.forecastNdaysFromNow
import com.example.app_team35.models.sources.Location
import com.example.app_team35.repositories.FavoriteStorage
import com.example.app_team35.viewmodels.LocationViewModel
import com.example.app_team35.viewmodels.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A fragment with Google Maps functionality
 */
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MainActivityViewModel
    private var forecastList: MutableList<LocationForecast> = mutableListOf()
    private var locationList: MutableList<Location> = mutableListOf()
    private var lastForecast: LocationForecast? = null
    private lateinit var favoriteStorage: FavoriteStorage
    private val LOCATION_PERMISSION_REQUEST_CODE = 2000
    lateinit var locationViewModel: LocationViewModel

    /**
     * Called when the fragment view has been created. Here we set up the map and
     * subscribe to model changes.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteStorage = FavoriteStorage(requireContext(), 3)
        val forecastDetailsView = getView()?.findViewById<RecyclerView>(R.id.forecast_details)

        prepRequestLocationUpdates()

        // Forecast view is initially hidden
        forecastDetailsView?.visibility = View.INVISIBLE

        // Hide forecast view when touched
        forecastDetailsView?.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {

                    forecastDetailsView.visibility = View.INVISIBLE
                }
            }

            v?.onTouchEvent(event) ?: true
        }

        // Instantiate the MapViewModel through a ViewModelProvider
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // Make sure we get notified once there's forecast data available
        viewModel.getForecastLiveData()?.observe(viewLifecycleOwner, Observer {
            // Update through adapter. Since we only show one item at a time for the selected marker,
            // we clear the list and then add the LocationForecast object.
            forecastList.clear()
            forecastList.add(it)
            this.lastForecast = it

            // Redraw and show the forecast pane
            forecastDetailsView?.adapter?.notifyDataSetChanged()
            forecastDetailsView?.visibility = View.VISIBLE

            // Example of using forecastNdaysFromNow, printing weather symbol code 7 days ahead for the timepoint closest to noon
            repeat(7) { index ->
                var timeseries = forecastNdaysFromNow(it, index + 1, 12)
                var symbolCode = timeseries?.data?.next6Hours?.summary?.symbolCode
                if (symbolCode != null) {
                    Log.d("MapFragment", "Symbol code is: " + symbolCode)
                }
            }
        })

        viewModel.getSourceQueryLiveData()?.observe(viewLifecycleOwner, Observer {
            locationList.clear()
            if (it.isNotEmpty()){
                locationList.add(it.removeFirst())

                Log.d("Source", "Source search complete")
                for (source in it) {
                    Log.d("Source", "Location name:" + source.name + ", lat: " + source.latitude + ", long: " + source.longitude);
                }
            }else{
                //Toast.makeText(activity?.applicationContext!!, "Did not find a match to your location search", Toast.LENGTH_LONG).show()
                Log.d("Source", "Location search did not return any hits")
            }
        })

        forecastDetailsView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // Attach adapter for updating the forecast pane
        val adapter = ForecastDetailsAdapter(forecastList)
        forecastDetailsView?.adapter = adapter

        // Set up map-ready notification
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationClick = getView()?.findViewById<ImageView>(R.id.location)

        var result = getView()?.findViewById<TextView>(R.id.text_view_id)
        locationClick?.setOnClickListener{
            Toast.makeText(activity?.applicationContext!!, "Using your location if location is enabled!", Toast.LENGTH_LONG).show()
            requestLocationUpdates()
            result?.setText("Bruker enhetens posisjon")
        }

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
    }

    /**
     * Called when Google Maps is ready
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapLongClickListener(this)

        // Sample markers
        val vikingSkipsHuset = LatLng(59.9292409,10.7384946)
        mMap.addMarker(MarkerOptions().position(vikingSkipsHuset).title("Vikingskipshuset"))
        mMap.addMarker(MarkerOptions().position(LatLng(60.1795242,11.0449892)).title("Oslo Lufthavn"))
        mMap.addMarker(MarkerOptions().position(LatLng(55.6777825,12.5918491)).title("København"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vikingSkipsHuset, 4f))

    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        Log.d("MapApp", "Position: latitude=" + marker?.position?.latitude + ", longitude: " + marker?.position?.longitude)

        // This will asynchronously load weather data from met.no. If data is available,
        // the viewModel.getTemperatureData()?.observe handler is invoked.
        viewModel.loadForecast(marker?.position?.latitude!!, marker?.position?.longitude!!, null)

        // Do default event handling as well (to center marker in map)
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        onMarkerClick(marker)
    }

    override fun onMapLongClick(newMarker: LatLng?) {
        if (newMarker != null) {
            viewModel.loadForecast(newMarker?.latitude!!, newMarker?.longitude!!, null)
            mMap.addMarker(MarkerOptions().position(LatLng(newMarker.latitude,newMarker.longitude)).title("Ny posisjon fra kart!"))

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
            // Check to make the activity testable
            if (this::mMap.isInitialized) {
                mMap.addMarker(MarkerOptions().position(latiLong).title("Din posisjon"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latiLong, 15f))
            }
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