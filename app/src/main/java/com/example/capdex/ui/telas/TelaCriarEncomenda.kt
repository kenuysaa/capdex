package com.example.capdex.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.capdex.ui.encomenda.CadastroEncomendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriarEncomenda(
    navController: NavHostController,
    viewModel: CadastroEncomendaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC))
            )
        )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Cadastrar Encomenda", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                LineTextField(value = uiState.encomenda, onValueChange = viewModel::onEncomendaChange, label = "Nome da encomenda")
                Spacer(modifier = Modifier.height(16.dp))
                LineTextField(value = uiState.remetenteCpf, onValueChange = viewModel::onRemetenteCpfChange, label = "CPF do remetente", keyboardType = KeyboardType.Number)
                Spacer(modifier = Modifier.height(16.dp))
                LineTextField(value = uiState.destinatarioCpf, onValueChange = viewModel::onDestinatarioCpfChange, label = "CPF do destinatário", keyboardType = KeyboardType.Number)
                Spacer(modifier = Modifier.height(16.dp))
                // Dropdown para selecionar embarcação
                val embarcacoes by viewModel.embarcacoes.collectAsState()
                var expanded by remember { mutableStateOf(false) }
                val embarcacaoSelecionada = embarcacoes.find { it.idEmbarcacao == uiState.embarcacaoId }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = embarcacaoSelecionada?.nomeEmbarcacao ?: "Selecione a embarcação",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Embarcação") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        embarcacoes.forEach { embarcacao ->
                            DropdownMenuItem(
                                text = { Text(embarcacao.nomeEmbarcacao) },
                                onClick = {
                                    viewModel.onEmbarcacaoSelecionada(embarcacao.idEmbarcacao)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.salvarEncomenda() },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF3E6340))
                    } else {
                        Text("Cadastrar", color = Color(0xFF3E6340), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                if (uiState.cadastroSucesso) {
                    Text("Encomenda cadastrada com sucesso!", color = Color(0xFF2E7D32), modifier = Modifier.padding(top = 16.dp))
                }
                uiState.erro?.let { erro ->
                    Text(erro, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
} 