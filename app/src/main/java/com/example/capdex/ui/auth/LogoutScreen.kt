package com.example.capdex.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    // Aplica o gradiente de fundo sobre toda a tela
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3E6340), // sombra escura
                        Color(0xFF9BFBE8), // intermediária
                        Color(0xFFC9FFF4), // clara
                        Color(0xFFC9FFF4),
                        Color(0xFFC9FFF4)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent, // <- evita fundo padrão do Scaffold
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Menu") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Deseja sair da sua conta?",
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        authViewModel.signOut()
                        onLogout()
                    }) {
                        Text("Sair")
                    }
                }
            }
        )
    }
}
