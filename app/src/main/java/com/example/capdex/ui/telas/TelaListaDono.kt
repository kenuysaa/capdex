package com.example.capdex.ui.telas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.capdex.ui.embarcacao.ListaEmbarcacoesViewModel
import com.example.capdex.ui.navigation.Screen

@Composable
fun EmbarcacaoDonoItem(
    id: String,
    nome: String,
    status: String,
    imagemUrl: String,
    onEditClick: () -> Unit
) {
    val corNomeEmbarcacao = Color(0xFF2E7D32)

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
            AsyncImage(
                model = imagemUrl,
                contentDescription = nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(nome, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = corNomeEmbarcacao)
                Text("ID: $id", fontSize = 12.sp, color = Color.Gray) // Exibe o id na interface
                Text(status, fontSize = 14.sp, color = Color.Gray)
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
fun TelaListaDono(
    navController: NavHostController,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    isFabExpanded: Boolean,
    onFabExpandedChange: (Boolean) -> Unit
) {
    val viewModel: ListaEmbarcacoesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Substitua "proprietario123" pelo ID do usuário logado, se necessário
    LaunchedEffect(Unit) {
        viewModel.carregarEmbarcacoes("proprietario123")
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC))
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Minhas Embarcações", color = Color.White, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabExpandedChange(!isFabExpanded) },
                shape = CircleShape,
                containerColor = Color(0xFF3E6340)
            ) {
                Icon(
                    imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Menu de Ações",
                    tint = Color.White
                )
            }
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
                    onClick = { onSelectedIndexChange(0) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, "Configurações") },
                    selected = selectedIndex == 1,
                    onClick = {
                        onSelectedIndexChange(1)
                        navController.navigate(Screen.Config.route)
                    }
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Log para debug
                println("Lista de embarcações: " + uiState.embarcacoes.joinToString { it.idEmbarcacao + ": " + it.nomeEmbarcacao })
                items(uiState.embarcacoes) { embarcacao ->
                    EmbarcacaoDonoItem(
                        id = embarcacao.idEmbarcacao,
                        nome = embarcacao.nomeEmbarcacao,
                        status = embarcacao.status,
                        imagemUrl = embarcacao.imagemUrl,
                        onEditClick = {
                            navController.navigate(Screen.EditarEmbarcacao.createRoute(embarcacao.idEmbarcacao))
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp, bottom = 90.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedVisibility(visible = isFabExpanded) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FabOption(
                            text = "Criar Rota",
                            onClick = { navController.navigate(Screen.CriarRota.route) }
                        )
                        FabOption(
                            text = "Criar Embarcação",
                            onClick = { navController.navigate(Screen.CriarEmbarcacao.route) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FabOption(text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold)
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color(0xFF3E6340),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = text)
        }
    }
}
