package com.example.capdex.ui.embarcacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.R
import com.example.capdex.data.model.Embarcacao
import com.example.capdex.data.repository.EmbarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListaEmbarcacoesUiState(
    val embarcacoes: List<Embarcacao> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ListaEmbarcacoesViewModel @Inject constructor(
    private val embarRepository: EmbarRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListaEmbarcacoesUiState())
    val uiState: StateFlow<ListaEmbarcacoesUiState> = _uiState

    fun carregarEmbarcacoes(proprietarioId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            // Simula carregamento
            kotlinx.coroutines.delay(1000)

            val lista = listOf(
                Embarcacao(
                    idEmbarcacao = "1",
                    nomeEmbarcacao = "Barco Correa Filho",
                    cnpj = "00000000000191",
                    imagemResId = R.drawable.barco_1,
                    nomeSetor = "Setor 1",
                    senhaSetor = "senha123",
                    pontoPartida = "Parintins",
                    pontoChegada = "Manaus",
                    proprietarioId = proprietarioId
                ),
                Embarcacao(
                    idEmbarcacao = "2",
                    nomeEmbarcacao = "Barco Príncipe Manoel",
                    cnpj = "00000000000272",
                    imagemResId = R.drawable.barco_2,
                    nomeSetor = "Setor 2",
                    senhaSetor = "senha456",
                    pontoPartida = "Pará",
                    pontoChegada = "Manaus",
                    proprietarioId = proprietarioId
                ),
                Embarcacao(
                    idEmbarcacao = "3",
                    nomeEmbarcacao = "Barco CapBarco",
                    cnpj = "00000000000363",
                    imagemResId = R.drawable.barco_3,
                    nomeSetor = "Setor 3",
                    senhaSetor = "senha789",
                    pontoPartida = "Parintins",
                    pontoChegada = "Manaus",
                    proprietarioId = proprietarioId
                )
            )

            _uiState.update { it.copy(embarcacoes = lista, isLoading = false) }
        }
    }
}
