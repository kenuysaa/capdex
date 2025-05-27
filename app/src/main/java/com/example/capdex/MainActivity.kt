package com.example.capdex

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.capdex.presentation.ui.theme.CapDexTheme
import com.example.capdex.ui.map.MapPreviewScreen

class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val coarseLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

        if (fineLocationGranted || coarseLocationGranted) {
            // Permissão concedida.
            // O MapPreviewScreen/MapViewModel tentará inicializar o LocationService.
            // Se o MapPreviewScreen já estiver na composição, seu LaunchedEffect
            // chamará initializeLocationServiceIfNeeded no ViewModel.
            // Se você precisar forçar uma re-verificação ou re-chamada no ViewModel,
            // você poderia ter um State no ViewModel que é atualizado aqui e observado na tela.
            // Mas, na maioria dos casos, a lógica atual deve ser suficiente.
        } else {
            // Permissão negada.
            // Você pode querer mostrar uma mensagem ao usuário ou desabilitar funcionalidades.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CapDexTheme {
                MainScreen()
            }
        }

        checkAndRequestLocationPermissions()
    }

    @Composable
    private fun MainScreen() {
        // MapPreviewScreen é o conteúdo principal que usará o ViewModel
        MapPreviewScreen()
    }

    private fun checkAndRequestLocationPermissions() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocationPermission || !hasCoarseLocationPermission) {
            // Solicita ambas as permissões se uma delas (ou ambas) não estiver concedida.
            // O usuário pode optar por conceder apenas a aproximada.
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        // Se as permissões já estiverem concedidas, o MapPreviewScreen/MapViewModel
        // irá lidar com a inicialização do serviço de localização quando for composto.
    }

    // Não há mais necessidade de startLocationUpdates() ou onDestroy() para o locationService aqui.
    // O ciclo de vida do LocationService agora está atrelado ao MapViewModel.
}