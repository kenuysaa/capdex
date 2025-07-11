package com.example.capdex.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capdex.ui.navigation.Screen

// Modelo de dados para cada pacote
data class Pacote(
    val id: Int,
    val destinatario: String,
    val nomeBarco: String,
    val destino: String,
    val tempoRestante: String
)

// Item individual da lista de pacotes
@Composable
fun PacoteItem(pacote: Pacote, mostrarSetaParaBaixo: Boolean) {
    val corNomePessoa = Color.White
    val corNomeBarco = Color(0xFFE0E0E0)
    val corCidade = Color(0xFFE0E0E0)
    val corSetaLaranja = Color(0xFFF5A623)

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(pacote.destinatario, color = corNomePessoa, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(pacote.nomeBarco, color = corNomeBarco, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (mostrarSetaParaBaixo) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                        contentDescription = "Direção",
                        tint = corSetaLaranja,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(pacote.destino, color = corCidade, fontSize = 14.sp)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.LocationOn, "Localização", tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Surface(color = Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                    Text(pacote.tempoRestante, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPacotes(
    navController: NavHostController,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit
) {
    val listaDePacotesEnviados = remember {
        listOf(
            Pacote(1, "Maria Cecilia Brito", "Barco Correa Filho", "Manaus", "15h:43m"),
            Pacote(2, "João Alfredo", "Barco Manoel", "Itacoatiara", "10h:12m"),
        )
    }
    val listaDePacotesRecebidos = remember {
        listOf(
            Pacote(3, "Carlos Eduardo", "Lancha Rápida", "Codajás", "3h:20m"),
            Pacote(4, "Fernanda Souza", "Ferry Boat", "Coari", "8h:15m")
        )
    }

    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Enviados", "Para você")
    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC)))

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pacotes", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp).height(68.dp).clip(RoundedCornerShape(50)),
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, "Embarcações") },
                    selected = selectedIndex == 0,
                    onClick = {
                        onSelectedIndexChange(0)
                        navController.popBackStack()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, "Pacotes") },
                    selected = selectedIndex == 1,
                    onClick = { onSelectedIndexChange(1) }
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(gradient).padding(innerPadding).padding(horizontal = 16.dp)
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = {},
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = tabIndex == index
                    val backgroundColor = if (isSelected) Color(0xFF3E6340) else Color.White.copy(alpha = 0.8f)
                    val textColor = if (isSelected) Color.White else Color.DarkGray

                    Tab(
                        selected = isSelected,
                        onClick = { tabIndex = index },
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(backgroundColor)
                    ) {
                        Text(text = title, color = textColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
                    }
                }
            }

            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                val pacotesParaExibir = if (tabIndex == 0) listaDePacotesEnviados else listaDePacotesRecebidos
                items(pacotesParaExibir) { pacote ->
                    PacoteItem(pacote = pacote, mostrarSetaParaBaixo = (tabIndex == 1))
                }
            }
        }
    }
}