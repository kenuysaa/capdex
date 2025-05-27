package com.example.capdex.ui.proprietario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capdex.presentation.OwnerVesselsViewModel

@Composable
fun OwnerVesselsScreen(ownerVesselsViewModel: OwnerVesselsViewModel = hiltViewModel()) {
    val uiState by ownerVesselsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Suas Embarcações",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text("Erro: ${uiState.errorMessage}", color = MaterialTheme.colorScheme.error)
        } else if (uiState.vessels.isEmpty()) {
            Text("Você ainda não cadastrou nenhuma embarcação.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.vessels) { vessel ->
                    VesselItem(vessel = vessel)
                }
            }
        }
    }
}

@Composable
fun VesselItem(vessel: com.example.capdex.domain.model.Embarcacao) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nome: ${vessel.nome}", style = MaterialTheme.typography.titleMedium)
            Text("CNPJ: ${vessel.cnpj}")
            Text("Telefone: ${vessel.telefone}")
            // Adicione mais detalhes conforme necessário
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OwnerVesselsScreenPreview() {
    MaterialTheme {
        OwnerVesselsScreen()
    }
}