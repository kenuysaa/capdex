package com.example.capdex.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriarRota(navController: NavHostController) {
    // ✅ 1. DEFINIÇÃO DO GRADIENTE
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC))
    )

    // ✅ 2. ESTRUTURA COM BOX PARA APLICAR O FUNDO GRADIENTE
    Box(modifier = Modifier.fillMaxSize().background(gradient)) {
        Scaffold(
            // ✅ 3. SCAFFOLD TRANSPARENTE
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Criar Rota", color = Color.White, fontWeight = FontWeight.Bold) },
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
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                DropdownInput(label = "Selecionar embarcação", options = listOf("Barco Príncipe Manoel", "Barco Adrenalina"))
                Spacer(modifier = Modifier.height(16.dp))
                DropdownInput(label = "Partida", options = listOf("Manaus", "Santarém", "Parintins"))
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        DropdownInput(label = "Parada (Somente se houver)", options = listOf("Itacoatiara", "Coari", "Codajás"))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Add, "Adicionar Parada", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                DropdownInput(label = "Destino", options = listOf("Manaus", "Santarém", "Parintins"))
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E6340))
                ) {
                    Text("Criar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInput(label: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    val corVerdePrincipal = Color(0xFF3E6340) // Cor para o texto

    Column {
        // ✅ 4. COR DO TEXTO DO LABEL ALTERADA PARA VERDE
        Text(label, color = corVerdePrincipal, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                placeholder = { Text("Selecione") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}