package com.example.capdex.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.capdex.presentation.MainScreenViewModel
import com.example.capdex.ui.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController, mainScreenViewModel: MainScreenViewModel = hiltViewModel()) {
    val uiState by mainScreenViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { navController.navigate(Screen.Map.route) }) {
                Text("Mapa")
            }
            Button(onClick = { navController.navigate(Screen.Logout.route) }) {
                Text("Logout")
            }
        }

        if (uiState.isProprietario) {
            Button(onClick = { navController.navigate(Screen.RegisterEmbarcacao.route) }) {
                Text("Cadastrar Embarcação")
            }
            Button(onClick = { navController.navigate(Screen.OwnerVessels.route) }) {
                Text("Minhas Embarcações")
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}