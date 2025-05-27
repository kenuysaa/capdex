package com.example.capdex.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.capdex.presentation.AuthViewModel

@Composable
fun LogoutScreen(authViewModel: AuthViewModel, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Deseja sair da sua conta?")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.signOut()
            onLogout() // Callback para navegar de volta para a tela de login
        }) {
            Text("Sair")
        }
    }
}