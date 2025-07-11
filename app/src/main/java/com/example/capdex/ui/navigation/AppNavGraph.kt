package com.example.capdex.ui.navigation

import androidx.compose.runtime.Composable
import com.example.capdex.ui.telas.TelaPacotes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capdex.ui.auth.AuthViewModel
import com.example.capdex.ui.auth.CadastroScreen
import com.example.capdex.ui.auth.LoginScreen
import com.example.capdex.ui.auth.LogoutScreen
import com.example.capdex.ui.embarcacao.CadastroEmbarcacaoViewModel
import com.example.capdex.ui.embarcacao.ListaEmbarcacoesViewModel
import com.example.capdex.ui.embarcacao.TelaConfiguracao

import com.example.capdex.ui.main.MainScreen
import com.example.capdex.ui.telas.TelaCriarEmbarcacao
import com.example.capdex.ui.telas.TelaCriarRota
import com.example.capdex.ui.telas.TelaEditarEmbarcacao
import com.example.capdex.ui.telas.TelaListaDono
import com.example.capdex.data.model.Pacote

@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()

    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var isFabExpanded by rememberSaveable { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Cadastro.route) {
            CadastroScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegistrationSuccess = { isDono ->
                    val destination = if (isDono) Screen.Dono.route else Screen.Main.route
                    navController.navigate(destination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToCadastro = { navController.navigate(Screen.Cadastro.route) },
                onLoginSuccess = { isDono ->
                    selectedIndex = 0
                    isFabExpanded = false
                    val destination = if (isDono) Screen.Dono.route else Screen.Main.route
                    navController.navigate(destination) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            val listaEmbarcacoesViewModel = hiltViewModel<ListaEmbarcacoesViewModel>()
            MainScreen(
                navController = navController,
                listaEmbarcacoesViewModel = listaEmbarcacoesViewModel,
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { newIndex -> selectedIndex = newIndex }
            )
        }

        composable(Screen.Dono.route) {
            TelaListaDono(
                navController = navController,
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { newIndex -> selectedIndex = newIndex },
                isFabExpanded = isFabExpanded,
                onFabExpandedChange = { isExpanded -> isFabExpanded = isExpanded }
            )
        }

        composable(Screen.Carga.route) {
            TelaPacotes(
                navController = navController,
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { newIndex -> selectedIndex = newIndex }
            )
        }

        composable(Screen.Config.route) {
            TelaConfiguracao(
                navController = navController,
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { newIndex -> selectedIndex = newIndex }
            )
        }

        composable(Screen.Logout.route) {
            LogoutScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.CriarEmbarcacao.route) {
            // ✅ O Hilt vai criar e fornecer o ViewModel automaticamente
            val cadastroViewModel = hiltViewModel<CadastroEmbarcacaoViewModel>()
            TelaCriarEmbarcacao(
                navController = navController,
                viewModel = cadastroViewModel
            )
        }
        composable(Screen.CriarRota.route) {
            TelaCriarRota(navController = navController)
        }

        composable(Screen.EditarEmbarcacao.route) { backStackEntry ->
            val embarcacaoId = backStackEntry.arguments?.getString("embarcacaoId")
            if (embarcacaoId != null) {
                TelaEditarEmbarcacao(
                    navController = navController,
                    embarcacaoId = embarcacaoId
                )
            } else {
                navController.popBackStack()
            }
        }
        composable(Screen.Pacotes.route) {
            TelaPacotes(
                navController = navController,
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { selectedIndex = it }
            )
        }
        composable(Screen.CriarEncomenda.route) {
            com.example.capdex.ui.telas.TelaCriarEncomenda(navController = navController)
        }


    }
}
