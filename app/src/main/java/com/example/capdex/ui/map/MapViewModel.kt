package com.example.capdex.ui.map

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.example.capdex.location.LocationService
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    // Mantém a instância do LocationService
    private var locationService: LocationService? = null

    // Flag para controlar a inicialização
    private var isLocationServiceInitialized = false

    fun initializeLocationServiceIfNeeded() {
        // Inicializa apenas uma vez e se as permissões estiverem (presumivelmente) concedidas
        // A verificação de permissão real é feita antes de chamar startLocationUpdates no LocationService
        if (!isLocationServiceInitialized) {
            locationService = LocationService(getApplication<Application>().applicationContext).apply {
                setOnLocationUpdateListener { location ->
                    updateLocation(location)
                }
                // A chamada para startLocationUpdates() é feita aqui,
                // o LocationService internamente verifica as permissões antes de realmente iniciar.
                startLocationUpdates()
            }
            isLocationServiceInitialized = true
        }
    }

    fun updateLocation(location: Location) {
        _currentLocation.value = LatLng(location.latitude, location.longitude)
    }

    // Chamado quando o ViewModel é destruído
    override fun onCleared() {
        super.onCleared()
        locationService?.stopLocationUpdates()
        locationService = null // Limpa a referência
        isLocationServiceInitialized = false
    }
}