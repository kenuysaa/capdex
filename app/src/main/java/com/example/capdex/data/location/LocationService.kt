package com.example.capdex.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.capdex.data.repository.EmbarRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class LocationService @Inject constructor( // Adicione @Inject aqui
    @ApplicationContext private val context: Context, // Use @ApplicationContext para o Contexto
    private val embarRepository: EmbarRepository // Injetar EmbarRepository
) {
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
                Log.d("LocationService", "Nova localização recebida: $location")
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
            Log.d("LocationService", "Iniciando atualizações de localização")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopLocationUpdates() {
        Log.d("LocationService", "Parando atualizações de localização")
        fusedLocationClient.removeLocationUpdates(locationCallback)
        onLocationUpdate = null
    }

    private suspend fun saveLocationToFirebase(location: Location) {
        try {
            Log.d("LocationService", "Salvando localização no Firebase: $location")
            val deviceModel = Build.MODEL

            val locationData = hashMapOf(
                "deviceModel" to deviceModel, // Adicionando o modelo do dispositivo
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
            Log.e("LocationService", "Erro ao salvar localização no Firebase", e)
            e.printStackTrace()
        }
    }
}