package com.example.capdex.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.capdex.data.model.Localizacao
import com.example.capdex.data.repository.EmbarRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationService @Inject constructor(
    private val context: Context,
    private val embarRepository: EmbarRepository
) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000) // Intervalo de 10 segundos
        .setMinUpdateIntervalMillis(5000) // Pelo menos a cada 5 segundos
        .build()

    private var onLocationUpdate: ((Location) -> Unit)? = null
    private var activeEmbarcacaoId: String? = null // ID da embarcação ativa

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                onLocationUpdate?.invoke(location)
                Log.d("LocationService", "Nova localização recebida: $location")

                // Verifica se há uma embarcação ativa e se ela está em movimento
                activeEmbarcacaoId?.let { embarcacaoId ->
                    if (isMoving(location)) {
                        coroutineScope.launch {
                            saveLocationForEmbarcacao(embarcacaoId, location)
                        }
                    } else {
                        Log.d("LocationService", "Embarcação $embarcacaoId não está em movimento, localização não será salva.")
                    }
                } ?: run {
                    Log.w("LocationService", "Nenhuma embarcação ativa definida para salvar localização.")
                }
            }
        }
    }

    fun setOnLocationUpdateListener(listener: (Location) -> Unit) {
        onLocationUpdate = listener
    }

    // Adicione o ID da embarcação ao iniciar as atualizações
    fun startLocationUpdates(embarcacaoId: String) {
        this.activeEmbarcacaoId = embarcacaoId
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationService", "Iniciando atualizações de localização para embarcação: $embarcacaoId")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            Log.e("LocationService", "Permissão de localização não concedida. Não foi possível iniciar atualizações.")
        }
    }

    fun stopLocationUpdates() {
        Log.d("LocationService", "Parando atualizações de localização")
        fusedLocationClient.removeLocationUpdates(locationCallback)
        onLocationUpdate = null
        activeEmbarcacaoId = null
    }

    private fun isMoving(location: Location): Boolean {
        return location.speed > 0.5 // m/s
    }

    private suspend fun saveLocationForEmbarcacao(embarcacaoId: String, location: Location) {
        try {
            Log.d("LocationService", "Salvando localização para embarcação $embarcacaoId: $location")

            val localizacaoData = Localizacao(
                latitude = location.latitude,
                longitude = location.longitude,
                timestamp = System.currentTimeMillis(),
                accuracy = location.accuracy.toDouble(),
                speed = location.speed.toDouble()
            )

            embarRepository.addLocalizacao(embarcacaoId, localizacaoData)

        } catch (e: Exception) {
            Log.e("LocationService", "Erro ao salvar localização para embarcação $embarcacaoId", e)
        }
    }
}