package com.example.capdex.ui.main

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.capdex.ui.embarcacao.ListaEmbarcacoesViewModel
import com.example.capdex.ui.navigation.Screen
import com.example.capdex.ui.telas.EmbarcacaoDono
import com.example.capdex.ui.telas.EmbarcacaoDonoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    listaEmbarcacoesViewModel: ListaEmbarcacoesViewModel = hiltViewModel(),
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit
) {
    val uiState by listaEmbarcacoesViewModel.uiState.collectAsState()
    var fabExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        listaEmbarcacoesViewModel.carregarEmbarcacoes("id_do_cliente_logado")
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF9BFBE8),
            Color(0xFFC9FFF4),
            Color(0xFFC9FFF4)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Minhas Embarcações", fontWeight = FontWeight.Bold, color = Color.White)
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .height(68.dp)
                        .clip(RoundedCornerShape(50)),
                    containerColor = Color.White
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.Sailing, "Embarcações") },
                        selected = selectedIndex == 0,
                        onClick = {
                            fabExpanded = false
                            onSelectedIndexChange(0)
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.Inventory2, "Pacotes") },
                        selected = selectedIndex == 1,
                        onClick = {
                            fabExpanded = false
                            onSelectedIndexChange(1)
                            navController.navigate(Screen.Carga.route)
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.Settings, "Configurações") },
                        selected = selectedIndex == 2,
                        onClick = {
                            fabExpanded = false
                            onSelectedIndexChange(2)
                            navController.navigate(Screen.Config.route)
                        }
                    )
                }
            },
            floatingActionButton = {
                if (selectedIndex == 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        if (fabExpanded) {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    "Criar Embarcação",
                                    modifier = Modifier
                                        .clickable {
                                            fabExpanded = false
                                            // Exemplo: navController.navigate(Screen.CriarEmbarcacao.route)
                                        }
                                        .padding(8.dp),
                                    color = Color.Black
                                )
                                Text(
                                    "Criar Rota",
                                    modifier = Modifier
                                        .clickable {
                                            fabExpanded = false
                                            // Exemplo: navController.navigate(Screen.CriarRota.route)
                                        }
                                        .padding(8.dp),
                                    color = Color.Black
                                )
                            }
                        }

                        FloatingActionButton(
                            onClick = { fabExpanded = !fabExpanded },
                            containerColor = Color(0xFF1B5E20),
                            contentColor = Color.White
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Adicionar")
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                items(uiState.embarcacoes) { embarcacao ->
                    val itemParaExibir = EmbarcacaoDono(
                        id = embarcacao.idEmbarcacao,
                        nome = embarcacao.nomeEmbarcacao,
                        status = "Disponível",
                        imagemResId = embarcacao.imagemResId
                    )

                    EmbarcacaoDonoItem(
                        embarcacao = itemParaExibir,
                        buttonText = "Editar",
                        onButtonClick = {
                            // Exemplo: navController.navigate(Screen.EditarEmbarcacao.createRoute(embarcacao.idEmbarcacao))
                        }
                    )
                }
            }
        }
    }
}
