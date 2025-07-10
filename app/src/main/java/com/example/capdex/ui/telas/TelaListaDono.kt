package com.example.capdex.ui.telas // Use o caminho correto do seu pacote

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capdex.R
import com.example.capdex.ui.navigation.Screen

// Modelo de dados e Item da lista (sem alteração)
data class EmbarcacaoDono(val id: String, val nome: String, val status: String, val imagemResId: Int)
@Composable
fun EmbarcacaoDonoItem(embarcacao: EmbarcacaoDono, onEditClick: () -> Unit) { /* ... */ }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaDono(navController: NavHostController) {
    val embarcacoes = remember {
        listOf(
            EmbarcacaoDono("1", "Barco Correa Filho", "Disponível", R.drawable.barco_1),
            EmbarcacaoDono("2", "Barco Príncipe Manoel", "Em viagem", R.drawable.barco_2)
        )
    }
    var isFabExpanded by remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }

    // ✅ DEFINIÇÃO DO GRADIENTE
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF8DE9C3),
            Color(0xFFB3F5DC)
        )
    )

    // ✅ ESTRUTURA DE FUNDO
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Scaffold(
            // ✅ SCAFFOLD TRANSPARENTE PARA MOSTRAR O FUNDO
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Minhas Embarcações", color = Color.White, fontWeight = FontWeight.Bold) },
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
                    NavigationBarItem(icon = { Icon(Icons.Outlined.Sailing, "Embarcações") }, selected = selectedIndex.value == 0, onClick = { /* Já está aqui */ })
                    NavigationBarItem(icon = { Icon(Icons.Outlined.Inventory2, "Pacotes") }, selected = selectedIndex.value == 1, onClick = { navController.navigate(Screen.Carga.route) })
                    NavigationBarItem(icon = { Icon(Icons.Outlined.Settings, "Configurações") }, selected = selectedIndex.value == 2, onClick = { navController.navigate(Screen.Config.route) })
                }
            }
        ) { innerPadding ->
            // O Box aqui dentro não é mais necessário, o Scaffold já tem o padding
            LazyColumn(
                modifier = Modifier.padding(innerPadding), // Apenas aplica o padding
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(embarcacoes) { embarcacao ->
                    EmbarcacaoDonoItem(embarcacao = embarcacao, onEditClick = { /*TODO*/ })
                }
            }

            // Coluna para o FAB
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                // ... Lógica do FAB ...
            }
        }
    }
}

// Componente auxiliar FabOption (sem alteração)
@Composable
fun FabOption(text: String, onClick: () -> Unit) { /* ... */ }