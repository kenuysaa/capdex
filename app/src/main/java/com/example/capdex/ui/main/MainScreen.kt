package com.example.capdex.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.capdex.ui.map.MapViewModel
import com.example.capdex.ui.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController, mainScreenViewModel: MainScreenViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val mapViewModel: MapViewModel = hiltViewModel()

    val uiState by mainScreenViewModel.uiState.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            mapViewModel.handleLocationPermissionResult(fineGranted, coarseGranted)

            if (fineGranted || coarseGranted) {
                navController.navigate(Screen.Map.route)
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        mapViewModel.handleLocationPermissionResult(
            fineGranted = hasFineLocationPermission,
            coarseGranted = hasCoarseLocationPermission
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Carregando tipo de usuário...", style = MaterialTheme.typography.bodyLarge)
        } else {
            Button(onClick = {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val isPermissionCurrentlyGranted = hasFineLocationPermission || hasCoarseLocationPermission

                if (isPermissionCurrentlyGranted) {
                    navController.navigate(Screen.Map.route)
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }) {
                Text("Ir para o Mapa")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(Screen.Logout.route)
            }) {
                Text("Sair")
            }

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}