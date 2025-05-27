package com.example.capdex.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPreviewScreen(mapViewModel: MapViewModel = hiltViewModel()) {
    val currentLocation by mapViewModel.currentLocation.collectAsState()
    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()

    // Estado da câmera do mapa, inicializado com uma localização padrão ou a localização atual se disponível.
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation ?: LatLng(-2.5489, -44.2778), 10f) // São Luís como padrão
    }

    // LaunchedEffect para iniciar/parar as atualizações de localização com base no status da permissão.
    // Ele é acionado sempre que 'locationPermissionGranted' muda.
    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            mapViewModel.startLocationUpdates()
        } else {
            mapViewModel.stopLocationUpdates()
        }
    }

    // LaunchedEffect para animar a câmera do mapa para a localização atual do usuário.
    // Ele é acionado sempre que 'currentLocation' muda.
    LaunchedEffect(currentLocation) {
        currentLocation?.let { latLng ->
            val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15f)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(cameraPosition),
                durationMs = 1000
            )
        }
    }

    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        // Adiciona um marcador na localização atual do usuário, se disponível.
        currentLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Sua Localização Atual",
                snippet = "Você está aqui!"
            )
        }
    }
}