package com.example.capdex.ui.dono // Ou o pacote de sua preferência

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

// Modelo de dados (sem alteração)
data class EmbarcacaoDono(
    val id: String,
    val nome: String,
    val status: String,
    val imagemResId: Int
)

// Componente para um item da lista
@Composable
fun EmbarcacaoDonoItem(embarcacao: EmbarcacaoDono, onEditClick: () -> Unit) {
    // ✅ 3. COR DO NOME DA EMBARCAÇÃO AGORA É VERDE E USA HEXADECIMAL
    val corNomeEmbarcacao = Color(0xFF2E7D32) // Verde Escuro

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = embarcacao.imagemResId),
                contentDescription = embarcacao.nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(embarcacao.nome, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = corNomeEmbarcacao)
                Text(embarcacao.status, fontSize = 14.sp, color = Color.Gray)
            }
            Button(
                onClick = onEditClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray.copy(alpha = 0.5f)
                )
            ) {
                Text("Editar", color = Color.DarkGray)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaDono(navController: NavHostController) { // Adicionado navController
    val embarcacoes = remember {
        listOf(
            EmbarcacaoDono("1", "Barco Correa Filho", "Disponível", R.drawable.barco_1),
            EmbarcacaoDono("2", "Barco Príncipe Manoel", "Em viagem", R.drawable.barco_2)
        )
    }
    var isFabExpanded by remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) } // Dono está na primeira tela
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF8DE9C3),
            Color(0xFFB3F5DC)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // ✅ 1. TÍTULO CENTRALIZADO
            CenterAlignedTopAppBar(
                title = { Text("Minhas Embarcações", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            // ✅ 2. BARRA DE NAVEGAÇÃO INFERIOR ADICIONADA
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(50)),
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, contentDescription = "Embarcações") },
                    selected = selectedIndex.value == 0,
                    onClick = { /* Já está aqui */ },
                    alwaysShowLabel = false
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Pacotes") },
                    selected = selectedIndex.value == 1,
                    onClick = { navController.navigate(Screen.Carga.route) },
                    alwaysShowLabel = false
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Configurações") },
                    selected = selectedIndex.value == 2,
                    onClick = { navController.navigate(Screen.Config.route) },
                    alwaysShowLabel = false
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(embarcacoes) { embarcacao ->
                    EmbarcacaoDonoItem(embarcacao = embarcacao, onEditClick = { /*TODO*/ })
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedVisibility(
                    visible = isFabExpanded,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it }
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        FabOption(text = "Criar Rota") { /*TODO*/ }
                        Spacer(modifier = Modifier.height(12.dp))
                        FabOption(text = "Criar Embarcação") { /*TODO*/ }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                FloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    shape = CircleShape,
                    containerColor = Color(0xFF3E6340)
                ) {
                    Icon(
                        imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Menu de Ações",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// Componente auxiliar (sem alteração)
@Composable
fun FabOption(text: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color(0xFF3E6340),
            modifier = Modifier.size(40.dp)
        ) {}
    }
}