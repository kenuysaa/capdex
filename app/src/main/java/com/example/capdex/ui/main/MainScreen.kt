package com.example.capdex.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.capdex.ui.theme.CapDexTheme
import com.example.capdex.MainActivity
import com.example.capdex.ui.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val mainActivity = context as? MainActivity

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            if (mainActivity?.checkLocationPermissions() == true) {
                navController.navigate(Screen.Map.route)
            } else {
                mainActivity?.requestLocationPermissions()
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
    }
}