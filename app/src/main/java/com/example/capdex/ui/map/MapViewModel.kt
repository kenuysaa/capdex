package com.example.capdex.ui.map

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.capdex.location.LocationService
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    private var locationService: LocationService? = null // Mantém a instância do LocationService

    fun startLocationUpdates() {
        Log.d("MapViewModel", "startLocationUpdates chamado. Permissão concedida: ${_locationPermissionGranted.value}")
        if (_locationPermissionGranted.value) {
            if (locationService == null) {
                Log.d("MapViewModel", "Inicializando LocationService")
                locationService = LocationService(getApplication<Application>().applicationContext).apply {
                    setOnLocationUpdateListener { location ->
                        updateLocation(location)
                    }
                    startLocationUpdates()
                }
            } else {
                Log.d("MapViewModel", "LocationService já inicializado, garantindo que as atualizações estão ativas")
                locationService?.startLocationUpdates()
            }
        }
    }

    fun stopLocationUpdates() {
        Log.d("MapViewModel", "stopLocationUpdates chamado")
        locationService?.stopLocationUpdates()
    }

    fun updateLocation(location: Location) {
        _currentLocation.value = LatLng(location.latitude, location.longitude)
    }

    fun handleLocationPermissionResult(fineGranted: Boolean, coarseGranted: Boolean) {
        val granted = fineGranted || coarseGranted
        Log.d("MapViewModel", "Resultado da permissão: $granted (fine: $fineGranted, coarse: $coarseGranted)")
        if (granted != _locationPermissionGranted.value) {
            _locationPermissionGranted.value = granted
            if (granted) {
                startLocationUpdates()
            } else {
                stopLocationUpdates()
                _currentLocation.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
        locationService = null
    }
}