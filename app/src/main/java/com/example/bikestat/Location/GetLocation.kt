package com.example.bikestat.Location

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationProvider
import android.location.LocationRequest
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

lateinit var locationCallback: LocationCallback
lateinit var locationProvider: FusedLocationProviderClient

@SuppressLint("MissingPermission")
@Composable
fun getLocation(context: Context): LatAndLong {
    locationProvider = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf(LatAndLong()) }

    DisposableEffect(key1 = locationProvider) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (location in result.locations) {
                    currentLocation = LatAndLong(location.latitude, location.longitude)
                }
                locationProvider.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val lat = location.latitude
                        val long = location.longitude
                        currentLocation = LatAndLong(lat, long)
                    }
                }
                    .addOnFailureListener {
                        Log.d("MyLog", "${it.message}")
                    }
            }
        }

        locationUpdate()

        onDispose {
            stopLocationUpdate()
        }

    }
    return currentLocation
}

fun stopLocationUpdate() {
    try {
        val removeTask = locationProvider.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MyLog", "Location CallBack removed")
            } else {
                Log.d("MyLog", "Failed to Location CallBack removed")
            }
        }
    } catch (se: SecurityException) {
        Log.d("MyLog", "Failed to remove Loc CallBack $se")
    }
}

@SuppressLint("MissingPermission")
fun locationUpdate() {
    locationCallback.let {
        val locationRequest: com.google.android.gms.location.LocationRequest =
            com.google.android.gms.location.LocationRequest.create().apply {
                interval = 1000
                fastestInterval = 1000
                maxWaitTime = 1000
                priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}

data class LatAndLong(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)