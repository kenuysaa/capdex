package com.example.capdex.ui.embarcacao

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capdex.presentation.EmbarcacaoRegistrationViewModel

@Composable
fun EmbarcacaoRegistrationScreen(
    embarcacaoRegistrationViewModel: EmbarcacaoRegistrationViewModel = hiltViewModel(),
    onEmbarcacaoRegistered: () -> Unit
) {
    val uiState by embarcacaoRegistrationViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Cadastro de Embarcação",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = uiState.nomeEmbarcacao,
            onValueChange = { embarcacaoRegistrationViewModel.onNomeEmbarcacaoChanged(it) },
            label = { Text("Nome da Embarcação") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.cnpj,
            onValueChange = { embarcacaoRegistrationViewModel.onCnpjChanged(it) },
            label = { Text("CNPJ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.telefone,
            onValueChange = { embarcacaoRegistrationViewModel.onTelefoneChanged(it) },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { embarcacaoRegistrationViewModel.registerEmbarcacao() },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Cadastrando..." else "Cadastrar Embarcação")
        }

        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        uiState.successMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.secondary)
            if (uiState.isEmbarcacaoRegistrada) {
                onEmbarcacaoRegistered()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmbarcacaoRegistrationScreenPreview() {
    MaterialTheme {
        EmbarcacaoRegistrationScreen(onEmbarcacaoRegistered = {})
    }
}