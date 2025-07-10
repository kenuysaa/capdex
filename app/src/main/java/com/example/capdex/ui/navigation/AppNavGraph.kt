package com.example.capdex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capdex.ui.auth.AuthViewModel
import com.example.capdex.ui.auth.CadastroScreen
import com.example.capdex.ui.auth.LoginScreen
import com.example.capdex.ui.auth.LogoutScreen
import com.example.capdex.ui.telas.TelaListaDono
import com.example.capdex.ui.embarcacao.ListaEmbarcacoesViewModel
import com.example.capdex.ui.embarcacao.TelaConfiguracao
import com.example.capdex.ui.embarcacao.TelaPacotes
import com.example.capdex.ui.main.MainScreen
import com.example.capdex.ui.map.MapPreviewScreen
import com.example.capdex.ui.map.MapViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel = hiltViewModel<AuthViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screen.Cadastro.route
    ) {

        composable(Screen.Cadastro.route) {
            CadastroScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                // 👇 2. AJUSTE NA NAVEGAÇÃO APÓS CADASTRO
                onRegistrationSuccess = { isDono ->
                    val destination = if (isDono) Screen.Dono.route else Screen.Main.route
                    navController.navigate(destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToCadastro = { navController.navigate(Screen.Cadastro.route) },
                // 👇 3. AJUSTE NA NAVEGAÇÃO APÓS LOGIN
                onLoginSuccess = { isDono ->
                    val destination = if (isDono) Screen.Dono.route else Screen.Main.route
                    navController.navigate(destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            val mapViewModel = hiltViewModel<MapViewModel>()
            val listaEmbarcacoesViewModel = hiltViewModel<ListaEmbarcacoesViewModel>()

            MainScreen(
                navController = navController,
                mapViewModel = mapViewModel,
                listaEmbarcacoesViewModel = listaEmbarcacoesViewModel
            )
        }

        // 👇 4. ADIÇÃO DA NOVA TELA COMO DESTINO
        composable(Screen.Dono.route) {
            TelaListaDono(navController = navController) // ✅ Passe o navController aqui
        }

        // --- Suas outras rotas continuam iguais ---

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
        composable(Screen.Carga.route) {
            TelaPacotes(navController = navController)
        }
        composable(Screen.Config.route) {
            TelaConfiguracao(navController = navController)
        }
    }
}