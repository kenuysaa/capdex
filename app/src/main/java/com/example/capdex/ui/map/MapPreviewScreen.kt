package com.example.capdex.ui.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPreviewScreen(
    navController: NavController,
    isProprietario: Boolean,
) {
    val mapViewModel: MapViewModel = hiltViewModel()
    val locationPermissionGranted by mapViewModel.locationPermissionGranted.collectAsState()
    val currentLocation by mapViewModel.currentLocation.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    // Launcher para pedir permissões
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            mapViewModel.handleLocationPermissionResult(fineGranted, coarseGranted)
        }
    )

    // Solicita permissão e move para localização padrão
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

    // Atualiza a câmera para o local atual se for proprietário
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
                    isMyLocationEnabled = true
                )
            ) {
                // Marcador para Porto Novo
                val portoNovoPosition = LatLng(-3.150333, -58.443555)
                val portoNovoState = rememberMarkerState(position = portoNovoPosition)
                MarkerInfoWindow(
                    state = portoNovoState,
                    title = "Porto Novo",
                    snippet = "Porto Novo",
                    onClick = {
                        portoNovoState.showInfoWindow()
                        true
                    }
                )

                // Marcador para Porto Velho
                val portoVelhoPosition = LatLng(-3.146670, -58.451048)
                val portoVelhoState = rememberMarkerState(position = portoVelhoPosition)
                MarkerInfoWindow(
                    state = portoVelhoState,
                    title = "Porto Velho",
                    snippet = "Porto Velho",
                    onClick = {
                        portoVelhoState.showInfoWindow()
                        true
                    }
                )

                // Exibe os InfoWindows ao carregar
                LaunchedEffect(Unit) {
                    portoNovoState.showInfoWindow()
                    portoVelhoState.showInfoWindow()
                }
            }

            // Botão flutuante para centralizar na localização do usuário
            if (isProprietario && currentLocation != null && locationPermissionGranted) {
                val coroutineScope = rememberCoroutineScope()

                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(currentLocation!!, 16f),
                                durationMs = 1000
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.MyLocation, contentDescription = "Minha Localização")
                }

            }
        }
    }
}