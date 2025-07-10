package com.example.capdex.ui.embarcacao

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext


// --- 1. Modelo de dados para cada pacote ---
data class Pacote(
    val id: Int,
    val destinatario: String,
    val nomeBarco: String,
    val destino: String,
    val tempoRestante: String
)

// --- 2. Item individual da lista de pacotes ---
// --- 2. Item individual da lista de pacotes ---
@Composable
fun PacoteItem(pacote: Pacote, mostrarSetaParaBaixo: Boolean) {
    // --- ✅ Cores personalizadas com código hexadecimal ---
    val corNomePessoa = Color(0xFF3E6340) // Branco
    val corNomeBarco = Color(0xFF848484)  // Cinza claro
    val corCidade = Color(0xFF818181)      // Outro tom de cinza claro

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Informações do pacote à esquerda
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pacote.destinatario,
                    // Usando a cor hexadecimal
                    color = corNomePessoa,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = pacote.nomeBarco,
                    // Usando a cor hexadecimal
                    color = corNomeBarco,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (mostrarSetaParaBaixo) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                        contentDescription = "Direção",
                        tint = Color(0xFFF5A623), // Exemplo de outra cor hexadecimal
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pacote.destino,
                        // Usando a cor hexadecimal
                        color = corCidade,
                        fontSize = 14.sp
                    )
                }
            }

            // Ícone e tempo restante à direita (continua igual)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Localização",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = pacote.tempoRestante,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = corNomePessoa, thickness = 1.dp)
    }
}

// --- 3. Tela principal de Pacotes ---
// --- 3. Tela principal de Pacotes ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPacotes(navController: NavHostController) {
    val context = LocalContext.current

    // ✅ Lista original para "Enviados" (5 itens)
    val listaDePacotesEnviados = remember {
        listOf(
            Pacote(1, "Maria Cecilia Brito", "Barco Correa Filho", "Manaus", "15h:43m"),
            Pacote(2, "João Alfredo", "Barco Manoel", "Itacoatiara", "10h:12m"),
            Pacote(3, "Geone Maia", "Barco Golfinho do Norte", "Parintins", "9h:3m"),
            Pacote(4, "Tanara", "Barco Parazinho", "Santarém", "48h:57m"),
            Pacote(5, "Clara CapDroid", "Barco Cultura", "Parintins", "57m")
        )
    }

    // ✅ Nova lista, menor, para "Para você" (3 itens)
    val listaDePacotesRecebidos = remember {
        listOf(
            Pacote(6, "Carlos Eduardo", "Lancha Rápida", "Codajás", "3h:20m"),
            Pacote(7, "Fernanda Souza", "Ferry Boat", "Coari", "8h:15m"),
            Pacote(8, "Lucas Pereira", "Expresso do Sol", "Tefé", "12h:45m"),
        )
    }

    // Estado para controlar qual aba está selecionada ("Enviados" ou "Para você")
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Enviados", "Para você")

    // O resto do código da função continua exatamente o mesmo...
    val selectedIndex = remember { mutableStateOf(1) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF9BFBE8),
            Color(0xFFC9FFF4),
            Color(0xFFC9FFF4)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Pacotes",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
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
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, contentDescription = "Embarcações") },
                    selected = selectedIndex.value == 0,
                    onClick = {
                        selectedIndex.value = 0
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    },
                    alwaysShowLabel = false
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Pacotes") },
                    selected = selectedIndex.value == 1,
                    onClick = { /* Já está nesta tela */ },
                    alwaysShowLabel = false
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Configurações") },
                    selected = selectedIndex.value == 2,
                    onClick = { selectedIndex.value = 2
                        navController.navigate(Screen.Config.route)},
                    alwaysShowLabel = false
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
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
                        Text(
                            text = title,
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }
            }

            // A lógica aqui já funciona corretamente com as listas separadas
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                val pacotesParaExibir = if (tabIndex == 0) listaDePacotesEnviados else listaDePacotesRecebidos
                items(pacotesParaExibir) { pacote ->
                    PacoteItem(pacote = pacote, mostrarSetaParaBaixo = (tabIndex == 1))
                }
            }
        }
    }
}