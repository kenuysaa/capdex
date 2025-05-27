package com.example.capdex.ui.map

import android.app.Application
import android.location.Location
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
        if (_locationPermissionGranted.value) {
            if (locationService == null) {
                locationService = LocationService(getApplication<Application>().applicationContext).apply {
                    setOnLocationUpdateListener { location ->
                        updateLocation(location)
                    }
                    // Inicia as atualizações. O LocationService deve ter sua própria verificação interna de permissão,
                    // mas aqui no ViewModel, já garantimos que _locationPermissionGranted.value é true.
                    startLocationUpdates()
                }
            } else {
                locationService?.startLocationUpdates() // Garanta que as atualizações estão ativas.
            }
        }
    }

    fun stopLocationUpdates() {
        locationService?.stopLocationUpdates()
    }

    fun updateLocation(location: Location) {
        _currentLocation.value = LatLng(location.latitude, location.longitude)
    }

    fun handleLocationPermissionResult(fineGranted: Boolean, coarseGranted: Boolean) {
        val granted = fineGranted || coarseGranted
        if (granted != _locationPermissionGranted.value) { // Atualiza apenas se houver mudança no status
            _locationPermissionGranted.value = granted
            if (granted) {
                startLocationUpdates() // Se a permissão foi concedida agora, inicie as atualizações.
            } else {
                stopLocationUpdates() // Se a permissão foi negada, pare as atualizações e limpe a localização.
                _currentLocation.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates() // Garante que as atualizações são paradas
        locationService = null // Limpa a referência para evitar vazamentos de memória
    }
}
