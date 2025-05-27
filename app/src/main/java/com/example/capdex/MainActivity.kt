package com.example.capdex

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.capdex.presentation.ui.theme.CapDexTheme
import com.example.capdex.ui.map.MapPreviewScreen
import com.example.capdex.ui.map.MapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mapViewModel: MapViewModel by viewModels()

    // Registra o callback para o resultado da solicitação de permissões de localização.
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val coarseLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        mapViewModel.handleLocationPermissionResult(fineLocationGranted, coarseLocationGranted) // Passa o resultado da permissão diretamente para o ViewModel.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CapDexTheme {
                MainScreen(mapViewModel = mapViewModel)
            }
        }

        checkAndRequestLocationPermissions() // Verifica e solicita permissões de localização no início do ciclo de vida da Activity.
    }

    @Composable
    private fun MainScreen(mapViewModel: MapViewModel) {
        MapPreviewScreen(mapViewModel = mapViewModel)
    }

    // Verifica o status atual das permissões de localização e as solicita se necessário.
    // Informa o ViewModel sobre o status inicial das permissões.
    private fun checkAndRequestLocationPermissions() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val initialGrantedState = hasFineLocationPermission || hasCoarseLocationPermission

        // Inicia o serviço de localização imediatamente se as permissões já estiverem concedidas.
        mapViewModel.handleLocationPermissionResult(hasFineLocationPermission, hasCoarseLocationPermission)

        if (!initialGrantedState) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}
