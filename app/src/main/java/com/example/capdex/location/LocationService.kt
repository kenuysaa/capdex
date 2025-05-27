package com.example.capdex.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class LocationService(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setMinUpdateIntervalMillis(5000)
        .build()

    private var onLocationUpdate: ((Location) -> Unit)? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                onLocationUpdate?.invoke(location)
                coroutineScope.launch {
                    saveLocationToFirebase(location)
                }
            }
        }
    }

    fun setOnLocationUpdateListener(listener: (Location) -> Unit) {
        onLocationUpdate = listener
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        onLocationUpdate = null
    }

    private suspend fun saveLocationToFirebase(location: Location) {
        try {
            val locationData = hashMapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "timestamp" to Date(),
                "accuracy" to location.accuracy,
                "speed" to location.speed
            )

            // Salvar no Firestore
            firestore.collection("locations")
                .document()
                .set(locationData)
                .await()

            // Salvar no Storage como backup
            val locationJson = locationData.toString()
            val storageRef = storage.reference
                .child("locations")
                .child("${Date().time}.json")

            storageRef.putBytes(locationJson.toByteArray()).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}