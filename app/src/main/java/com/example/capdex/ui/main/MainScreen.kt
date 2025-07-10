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
import androidx.compose.material.icons.filled.Inventory2
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
    val context = LocalContext.current
    val selectedIndex = remember { mutableStateOf(0) }
    val uiState by listaEmbarcacoesViewModel.uiState.collectAsState()

    // Este LaunchedEffect continua igual, carregando os dados
    LaunchedEffect(Unit) {
        listaEmbarcacoesViewModel.carregarEmbarcacoes("proprietario123")
    }

    // As lógicas de permissão também continuam as mesmas
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            mapViewModel.handleLocationPermissionResult(fineGranted, coarseGranted)
            if (fineGranted || coarseGranted) {
                navController.navigate(Screen.Map.route)
            }
        }
    )
    LaunchedEffect(Unit) {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        mapViewModel.handleLocationPermissionResult(
            fineGranted = hasFineLocationPermission,
            coarseGranted = hasCoarseLocationPermission
        )
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF9BFBE8),
            Color(0xFFC9FFF4),
            Color(0xFFC9FFF4)
        )
    )

    //  Scaffold para estruturar a tela
    Scaffold(
        // Scaffold transparente para o gradiente do Box aparecer
        containerColor = Color.Transparent,
        topBar = {
            //  BARRA SUPERIOR
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Embarcações",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Fecha o app ou volta para a tela anterior
                        (context as? Activity)?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Sair do App",
                            tint = Color.White
                        )
                    }
                },
                // Deixamos a barra transparente para manter o design
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            //  BARRA INFERIOR COM BORDAS OVAIS
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp) // Aumenta o espaçamento
                    .height(68.dp)
                    .clip(RoundedCornerShape(50)), // Arredonda as bordas
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, contentDescription = "Embarcações") },
                    selected = selectedIndex.value == 0,
                    onClick = { selectedIndex.value = 0 },
                    alwaysShowLabel = false
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Cargas") },
                    selected = selectedIndex.value == 1,
                    onClick = {
                        selectedIndex.value = 1
                        navController.navigate(Screen.Carga.route)
                    },
                    alwaysShowLabel = false
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Configurações") },
                    selected = selectedIndex.value == 2,
                    onClick = { selectedIndex.value = 2 /* Navegar para config */
                                 navController.navigate(Screen.Config.route)
                              },
                    alwaysShowLabel = false
                )
            }
        }
    ) { innerPadding -> // O Scaffold fornece o padding necessário
        // O Box com o gradiente agora envolve o Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding) // Aplica o padding para não sobrepor as barras
        ) {
            // Usamos LazyColumn para melhor performance da lista
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        "Hoje",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                if (uiState.isLoading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.wrapContentSize(Alignment.Center))
                    }
                } else {
                    items(uiState.embarcacoes) { embarcacao ->
                        EmbarcacaoCard(
                            nome = embarcacao.nomeEmbarcacao,
                            rota = "${embarcacao.pontoPartida} -> ${embarcacao.pontoChegada}",
                            horario = "6h da manhã",
                            status = null,
                            imagemUrl = embarcacao.imagemResId,
                            onClick = { navController.navigate(Screen.Carga.route) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (uiState.errorMessage != null) {
                    item {
                        Text(
                            text = uiState.errorMessage!!,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}