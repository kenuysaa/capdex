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
import com.example.capdex.data.model.Pacote
import com.example.capdex.ui.navigation.Screen
import com.example.capdex.data.model.PacotesViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capdex.ui.encomenda.ListaEncomendasRemetenteViewModel
import com.example.capdex.ui.encomenda.ListaEncomendasDestinatarioViewModel
import com.example.capdex.ui.auth.AuthViewModel

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
    onSelectedIndexChange: (Int) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModelRemetente: ListaEncomendasRemetenteViewModel = hiltViewModel(),
    viewModelDestinatario: ListaEncomendasDestinatarioViewModel = hiltViewModel()
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Enviados", "Pra você")
    val gradient = Brush.verticalGradient(colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC)))

    val authUiState by authViewModel.uiState.collectAsState()
    val cpfUsuario = authUiState.cpf

    val uiStateRemetente by viewModelRemetente.uiState.collectAsState()
    val uiStateDestinatario by viewModelDestinatario.uiState.collectAsState()

    // Buscar encomendas ao trocar de aba ou quando o CPF mudar
    LaunchedEffect(tabIndex, cpfUsuario) {
        if (cpfUsuario.isNotBlank()) {
            if (tabIndex == 0) {
                viewModelRemetente.carregarEncomendasPorRemetente(cpfUsuario)
            } else {
                viewModelDestinatario.carregarEncomendasPorDestinatario(cpfUsuario)
            }
        }
    }

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
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = tabIndex == index
                    val backgroundColor = if (isSelected) Color(0xFF3E6340) else Color.White.copy(alpha = 0.8f)
                    val textColor = if (isSelected) Color.White else Color.DarkGray

                    Button(
                        onClick = { tabIndex = index },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundColor,
                            contentColor = textColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(40.dp),
                        elevation = null
                    ) {
                        Text(
                            text = title,
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val encomendas = if (tabIndex == 0) uiStateRemetente.encomendas else uiStateDestinatario.encomendas
            val isLoading = if (tabIndex == 0) uiStateRemetente.isLoading else uiStateDestinatario.isLoading
            val errorMessage = if (tabIndex == 0) uiStateRemetente.errorMessage else uiStateDestinatario.errorMessage

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (!errorMessage.isNullOrBlank()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    if (encomendas.isEmpty()) {
                        item {
                            Text("Nenhuma encomenda encontrada", color = Color.White, modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        items(encomendas) { encomenda ->
                            // Aqui você pode criar um card customizado para a encomenda
                            // Exemplo simples:
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Encomenda: ${encomenda.encomenda}", color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Status: ${encomenda.status}", color = Color.White)
                                    Text("Embarcação: ${encomenda.embarcacaoId}", color = Color.White)
                                    Text("Remetente: ${encomenda.remetenteCpf}", color = Color.White)
                                    Text("Destinatário: ${encomenda.destinatarioCpf}", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
