package com.example.capdex.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.capdex.ui.auth.AuthViewModel
import com.example.capdex.ui.auth.LoginScreen
import com.example.capdex.ui.auth.LogoutButton
import com.example.capdex.ui.auth.RegisterScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val currentUserUid by authViewModel.currentUserUid.collectAsState()

    // Define routes
    val REGISTER_ROUTE = "register_screen"
    val LOGIN_ROUTE = "login_screen"
    val HOME_ROUTE = "home_screen" // Your main application screen

    // Determine the starting destination
    val startDestination = if (currentUserUid != null) HOME_ROUTE else LOGIN_ROUTE

    NavHost(navController = navController, startDestination = startDestination) {
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                onRegistrationSuccess = { uid ->
                    // After successful registration (and user data added), navigate to home
                    navController.navigate(HOME_ROUTE) {
                        popUpTo(REGISTER_ROUTE) { inclusive = true } // Clear back stack
                        popUpTo(LOGIN_ROUTE) { inclusive = true } // Also clear login if it was there
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(REGISTER_ROUTE) { inclusive = true } // Replace register with login
                    }
                }
            )
        }
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = { uid ->
                    // After successful login, navigate to home
                    navController.navigate(HOME_ROUTE) {
                        popUpTo(LOGIN_ROUTE) { inclusive = true } // Clear back stack
                        popUpTo(REGISTER_ROUTE) { inclusive = true } // Also clear register if it was there
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(REGISTER_ROUTE) {
                        popUpTo(LOGIN_ROUTE) { inclusive = true } // Replace login with register
                    }
                }
            )
        }
        composable(HOME_ROUTE) {
            // This is your main application screen for logged-in users
            HomeScreenContent(onLogout = {
                // When logout is successful, navigate back to login
                navController.navigate(LOGIN_ROUTE) {
                    popUpTo(HOME_ROUTE) { inclusive = true } // Clear home screen from back stack
                }
            })
        }
    }
}

@Composable
fun HomeScreenContent(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à sua tela principal!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        LogoutButton(onLogoutSuccess = onLogout) // Use the separate LogoutButton composable
    }
}