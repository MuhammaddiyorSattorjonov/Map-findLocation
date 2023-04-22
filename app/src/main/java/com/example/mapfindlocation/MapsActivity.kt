package com.example.mapfindlocation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mapfindlocation.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition

private const val TAG = "MapsActivity"
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun findLocation() {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        val location = fusedLocationProvider.lastLocation

        location.addOnSuccessListener {
            Log.d(TAG, "findLocation: $it")
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()

            mMap.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)))

            val camera = CameraPosition.Builder()
                .target(LatLng(it.latitude,it.longitude))
                .zoom(18f)
                .bearing(it.bearing)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
        }.addOnFailureListener {
            Toast.makeText(this, "Error${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType =  GoogleMap.MAP_TYPE_NORMAL

        findLocation()
    }
}