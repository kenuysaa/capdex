package com.example.capdex.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Main : Screen("main")
    object Map : Screen("map")
    object Logout : Screen("logout")
    object RegisterEmbarcacao : Screen("register_embarcacao")
    object OwnerVessels : Screen("owner_vessels")
}

sealed class MainScreenRoute(val route: String) {
    object Map : MainScreenRoute("map")
    object Logout : MainScreenRoute("logout")
}