package com.example.capdex.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Main : Screen("main")
    object Map : Screen("map")
    object Logout : Screen("logout")
    object Carga : Screen("carga")
    object Config : Screen("config")
    object Dono : Screen("dono_lista")
    object CriarEmbarcacao : Screen("criar_embarcacao")
    object CriarRota : Screen("criar_rota")
    object Pacotes : Screen("pacotes")
    object EditarEmbarcacao : Screen("editar_embarcacao/{embarcacaoId}") {
        fun createRoute(embarcacaoId: String) = "editar_embarcacao/$embarcacaoId"
    }
}

