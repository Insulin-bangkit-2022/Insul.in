package com.insulin.app.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.insulin.app.R
import com.insulin.app.data.model.Hospital
import com.insulin.app.databinding.ActivityMapsBinding
import com.insulin.app.databinding.CustomTooltipHospitalBinding
import com.insulin.app.utils.Helper


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnCloseAlert.setOnClickListener {
            binding.containerAlert.isVisible = false
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hospital_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            /* move maps to user location */
            R.id.navigation_user_location -> {
                getMyLastLocation()
            }

            /* toggle show / hide container alert */
            R.id.navigation_help -> {
                binding.containerAlert.isVisible = !binding.containerAlert.isVisible
            }
        }
        return true
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.setInfoWindowAdapter(this)

        /* when user click hospital marker -> show info hospital */
        mMap.setOnPoiClickListener {
            val hospital = Hospital(
                name = it.name.replace("\n", " "),
                placeId = it.placeId,
                address = Helper.parseAddressLocation(
                    context = this,
                    lat = it.latLng.latitude,
                    lon = it.latLng.longitude
                ),
                lon = it.latLng.longitude,
                lat = it.latLng.longitude
            )
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            it.latLng.latitude,
                            it.latLng.longitude
                        )
                    )
            ).let { marker ->
                marker?.tag = hospital
                marker?.showInfoWindow()
            }
        }

        /* when popup hospital clicked */
        mMap.setOnInfoWindowClickListener {
            val hospital: Hospital = it.tag as Hospital
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${hospital.name}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        setMapStyle()
        getMyLastLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    /* get user location */
    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                /* if user location fetched -> add marker & trigger input to user location */
                location?.let {
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f)
                    )
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this,
                        R.raw.map_hospital
                    )
                )
            if (!success) {
                Log.e("MAPS", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MAPS", "Can't find style. Error: ", exception)
        }
    }

    override fun getInfoWindow(marker: Marker): View {
        val bindingTooltips =
            CustomTooltipHospitalBinding.inflate(LayoutInflater.from(this))
        val hospital: Hospital = marker.tag as Hospital
        bindingTooltips.hospitalName.text = StringBuilder("🏨 ").append(hospital.name)
        bindingTooltips.hospitalAddress.text = hospital.address
        return bindingTooltips.root
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}