package com.example.capdex.ui.map

import android.util.Log
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
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPreviewScreen(isProprietario: Boolean, mapViewModel: MapViewModel = hiltViewModel()) {
    val currentLocation by mapViewModel.currentLocation.collectAsState()
    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentLocation ?: LatLng(-2.5489, -44.2778),
            10f
        )
    }

    // Inicia ou para as atualizações de localização com base no status da permissão e tipo de usuário
    LaunchedEffect(locationPermissionGranted, isProprietario) {
        Log.d("MapPreviewScreen", "isProprietario: $isProprietario, locationPermissionGranted: $locationPermissionGranted")
        if (isProprietario) {
            if (locationPermissionGranted) {
                mapViewModel.startLocationUpdates()
            } else {
                mapViewModel.stopLocationUpdates()
            }
        } else {
            mapViewModel.stopLocationUpdates()
            val demoRouteCenter = LatLng(-3.150333, -58.443555)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(demoRouteCenter, 12f),
                durationMs = 1000
            )
        }
    }

    // Anima a câmera para a localização atual do proprietário
    LaunchedEffect(currentLocation, isProprietario) {
        if (isProprietario) {
            currentLocation?.let { latLng ->
                val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15f)
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(cameraPosition),
                    durationMs = 1000
                )
            }
        }
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = com.google.maps.android.compose.MapProperties(
            isMyLocationEnabled = isProprietario && locationPermissionGranted
        )
    ) {
        if (isProprietario) {
            // A bolinha azul já indica a localização
        } else {
            val demoRoutePoints = listOf(
                LatLng(-3.150333, -58.443555),
                LatLng(-3.146670, -58.451048),
                LatLng(-3.16, -58.43)
            )
            Polyline(points = demoRoutePoints, color = androidx.compose.ui.graphics.Color.Blue, width = 5f)
            demoRoutePoints.forEachIndexed { index, latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Ponto ${index + 1}",
                    snippet = "Ponto de Demonstração"
                )
            }
        }
    }
}