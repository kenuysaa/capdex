package com.example.capdex.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.capdex.ui.components.EmbarcacaoCard
import com.example.capdex.ui.embarcacao.ListaEmbarcacoesViewModel
import com.example.capdex.ui.map.MapViewModel
import com.example.capdex.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    mapViewModel: MapViewModel = hiltViewModel(),
    listaEmbarcacoesViewModel: ListaEmbarcacoesViewModel = hiltViewModel()
) {
    val uiState by listaEmbarcacoesViewModel.uiState.collectAsState()
    val selectedIndex = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        listaEmbarcacoesViewModel.carregarEmbarcacoes("proprietario123")
    }

    // ✅ DEFINIÇÃO DO GRADIENTE
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF9BFBE8),
            Color(0xFFC9FFF4),
            Color(0xFFC9FFF4)
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
                    title = {
                        Text(
                            "Embarcações",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // ... conteúdo da lista
            }
        }
    }
}