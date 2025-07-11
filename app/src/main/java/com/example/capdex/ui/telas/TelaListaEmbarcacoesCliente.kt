package com.example.capdex.ui.telas // Verifique se o pacote está correto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.capdex.ui.components.EmbarcacaoCard
import com.example.capdex.ui.embarcacao.ListaEmbarcacoesViewModel
import com.example.capdex.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaEmbarcacoesCliente(
    navController: NavHostController,
    viewModel: ListaEmbarcacoesViewModel = hiltViewModel(),
    // ✅ 1. PARÂMETROS ADICIONADOS PARA CONTROLAR A BARRA DE NAVEGAÇÃO
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        // Esta função agora deve vir do seu repositório real
        // viewModel.carregarTodasEmbarcacoes()
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3E6340), Color(0xFF9BFBE8), Color(0xFFC9FFF4), Color(0xFFC9FFF4))
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Embarcações", fontWeight = FontWeight.Bold, color = Color.White) },
                // O botão de voltar pode ser opcional se esta for a tela principal do cliente
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        // ✅ 2. BARRA DE NAVEGAÇÃO INFERIOR ADICIONADA
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
                    onClick = { onSelectedIndexChange(0) } // Apenas atualiza o estado
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, "Pacotes") },
                    selected = selectedIndex == 1,
                    onClick = {
                        onSelectedIndexChange(1)
                        navController.navigate(Screen.Carga.route)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, "Configurações") },
                    selected = selectedIndex == 2,
                    onClick = {
                        onSelectedIndexChange(2)
                        navController.navigate(Screen.Config.route)
                    }
                )
            }
        },
        // O FloatingActionButton foi removido para dar lugar à barra de navegação
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (!uiState.errorMessage.isNullOrBlank()) {
                Text(uiState.errorMessage ?: "Erro ao carregar embarcações", color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (uiState.embarcacoes.isEmpty()) {
                        item {
                            Text("Nenhuma embarcação encontrada", color = Color.White, modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        items(uiState.embarcacoes) { embarcacao ->
                            EmbarcacaoCard(
                                nome = embarcacao.nomeEmbarcacao,
                                rota = "${embarcacao.pontoPartida} -> ${embarcacao.pontoChegada}",
                                horario = "Horário a definir",
                                status = embarcacao.status,
                                imagemUrl = embarcacao.imagemUrl,
                                onClick = { /* Pode abrir detalhes se desejar */ }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}
