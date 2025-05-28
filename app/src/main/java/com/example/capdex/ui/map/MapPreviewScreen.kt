@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.capdex.ui.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapPreviewScreen(
    navController: NavController,
    isProprietario: Boolean,
    mapViewModel: MapViewModel = viewModel()
) {
    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()
    val currentLocation by mapViewModel.currentLocation.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            mapViewModel.handleLocationPermissionResult(fineGranted, coarseGranted)
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                mapViewModel.defaultLocation,
                15f
            )
        )
    }

    // Move a câmera para a localização atual do proprietário assim que ela estiver disponível
    LaunchedEffect(currentLocation, isProprietario) {
        if (isProprietario && currentLocation != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(currentLocation!!, 15f),
                durationMs = 1000
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Visualização do Mapa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = isProprietario && locationPermissionGranted
                )

            )
            // Marcador para Porto Novo
//            Marker(
//                state = MarkerState(position = LatLng(-3.150333, -58.443555)),
//                title = "Porto Novo",
//                snippet = "Porto Novo"
//            )
//
//            // Marcador para Porto Velho
//            Marker(
//                state = MarkerState(position = LatLng(-3.146670, -58.451048)),
//                title = "Porto Velho",
//                snippet = "Porto Velho"
//            )
        }
    }
}