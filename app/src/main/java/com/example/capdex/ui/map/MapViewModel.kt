package com.example.capdex.ui.map

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.capdex.data.location.LocationService
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    application: Application,
    private val locationService: LocationService
) : AndroidViewModel(application) {

    val defaultLocation = LatLng(-23.55052, -46.633308) // São Paulo, Brasil
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    private val _activeEmbarcacaoId = MutableStateFlow<String?>("ID_DA_EMBARCACÃO_ATIVA_AQUI")

    init {
        locationService.setOnLocationUpdateListener { location ->
            updateLocation(location)
        }
    }

    private fun startLocationUpdates() {
        Log.d("MapViewModel", "startLocationUpdates chamado. Permissão concedida: ${_locationPermissionGranted.value}")
        if (_locationPermissionGranted.value) {
            _activeEmbarcacaoId.value?.let { embarcacaoId ->
                Log.d("MapViewModel", "Iniciando LocationService para embarcação: $embarcacaoId")
                locationService.startLocationUpdates(embarcacaoId)
            } ?: run {
                Log.w("MapViewModel", "Não foi possível iniciar atualizações: ID da embarcação ativa não definido.")
            }
        }
    }

    private fun stopLocationUpdates() {
        Log.d("MapViewModel", "stopLocationUpdates chamado")
        locationService.stopLocationUpdates()
    }

    private fun updateLocation(location: Location) {
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