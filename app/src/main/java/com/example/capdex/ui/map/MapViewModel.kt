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
import kotlinx.coroutines.launch
import com.example.capdex.data.model.Embarcacao
import com.example.capdex.data.model.Localizacao
import com.example.capdex.data.repository.EmbarRepository
import javax.inject.Inject

// Estado para exibir embarcações e localizações no mapa
data class EmbarcacaoComLocalizacao(
    val embarcacao: Embarcacao,
    val localizacao: Localizacao?
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    private val embarRepository: EmbarRepository
) : ViewModel() {

    val defaultLocation = com.google.android.gms.maps.model.LatLng(-23.55052, -46.633308)

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    private val _embarcacoesComLocalizacao = MutableStateFlow<List<EmbarcacaoComLocalizacao>>(emptyList())
    val embarcacoesComLocalizacao: StateFlow<List<EmbarcacaoComLocalizacao>> = _embarcacoesComLocalizacao

    private val _isLoadingEmbarcacoes = MutableStateFlow(false)
    val isLoadingEmbarcacoes: StateFlow<Boolean> = _isLoadingEmbarcacoes

    private val _errorEmbarcacoes = MutableStateFlow<String?>(null)
    val errorEmbarcacoes: StateFlow<String?> = _errorEmbarcacoes

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

    fun carregarEmbarcacoesNoMapa() {
        _isLoadingEmbarcacoes.value = true
        _errorEmbarcacoes.value = null
        viewModelScope.launch {
            try {
                val embarcacoes = embarRepository.getTodasEmbarcacoes()
                val lista = embarcacoes.map { embarcacao ->
                    val localizacoes = embarRepository.getLatestLocalizacoes(embarcacao.idEmbarcacao, 1)
                    EmbarcacaoComLocalizacao(
                        embarcacao = embarcacao,
                        localizacao = localizacoes.firstOrNull()
                    )
                }
                _embarcacoesComLocalizacao.value = lista
                _isLoadingEmbarcacoes.value = false
            } catch (e: Exception) {
                _errorEmbarcacoes.value = e.localizedMessage ?: "Erro ao carregar embarcações"
                _isLoadingEmbarcacoes.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}