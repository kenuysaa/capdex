package com.example.capdex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capdex.ui.auth.CadastroScreen
import com.example.capdex.ui.auth.LoginScreen
import com.example.capdex.ui.auth.LogoutScreen
import com.example.capdex.ui.main.MainScreen
import com.example.capdex.ui.map.MapPreviewScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel = hiltViewModel<com.example.capdex.ui.auth.AuthViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screen.Cadastro.route
    ) {

        composable(Screen.Cadastro.route) {
            CadastroScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegistrationSuccess = { navController.navigate(Screen.Main.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToCadastro = { navController.navigate(Screen.Cadastro.route) },
                onLoginSuccess = { navController.navigate(Screen.Main.route) }
            )
        }

        composable(Screen.Main.route) {
            val mainScreenViewModel = hiltViewModel<com.example.capdex.ui.main.MainScreenViewModel>()
            MainScreen(
                navController = navController,
                mainScreenViewModel = mainScreenViewModel
            )
        }

        composable(Screen.Map.route) {
            val mainScreenViewModel = hiltViewModel<com.example.capdex.ui.main.MainScreenViewModel>()
            val uiState by mainScreenViewModel.uiState.collectAsState()
            MapPreviewScreen(
                navController = navController,
                isProprietario = uiState.isProprietario
            )
        }

        composable(Screen.Logout.route) {
            LogoutScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
