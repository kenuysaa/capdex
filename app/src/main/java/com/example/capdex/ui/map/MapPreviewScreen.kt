package com.example.capdex.ui.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapPreviewScreen() {
    // viewModel() factory padrão funciona para AndroidViewModel também
    val viewModel: MapViewModel = viewModel()
    val currentLocation by viewModel.currentLocation.collectAsState()

    // Inicializa o serviço de localização quando o composable entra na composição
    // A lógica de inicializar apenas uma vez está agora dentro do ViewModel
    LaunchedEffect(Unit) {
        viewModel.initializeLocationServiceIfNeeded()
    }

    val defaultLocation = LatLng(-23.550520, -46.633308) // São Paulo (Fallback)
    val cameraPositionState = rememberCameraPositionState {
        // Posição inicial da câmera
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f) // Zoom inicial mais afastado
    }

    // Efeito para mover a câmera quando a localização atual mudar
    LaunchedEffect(currentLocation) {
        Log.d("MapPreviewScreen", "Current Location: $currentLocation")
        currentLocation?.let { newLocation ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(newLocation, 15f, 0f, 0f) // Tilt, Bearing = 0
                ),
                durationMs = 1000
            )
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true // Mostra o ponto azul se a permissão estiver concedida e a localização ativa
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true, // Habilita controles de zoom
                    myLocationButtonEnabled = true // Habilita o botão "minha localização"
                )
            ) {
                currentLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Localização Atual",
                        snippet = "Você está aqui"
                    )
                }
                // Você pode adicionar outros Markers aqui se necessário
            }
        }
    }
}