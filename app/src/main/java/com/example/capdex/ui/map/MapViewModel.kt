package com.example.capdex.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.capdex.data.location.LocationService
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService
) : ViewModel() {

    val defaultLocation = com.google.android.gms.maps.model.LatLng(-23.55052, -46.633308)

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    fun startLocationUpdates() {
        Log.d("MapViewModel", "startLocationUpdates chamado. Permissão concedida: ${_locationPermissionGranted.value}")
        if (_locationPermissionGranted.value) {
            locationService.setOnLocationUpdateListener { location ->
                updateLocation(location)
            }
            locationService.startLocationUpdates()
        }
    }

    fun stopLocationUpdates() {
        Log.d("MapViewModel", "Parando atualizações")
        locationService.stopLocationUpdates()
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
    }
}