package com.example.capdex.ui.embarcacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.model.Embarcacao
import com.example.capdex.data.repository.EmbarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI para a lista de embarcações
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
            try {
                val lista = embarRepository.getEmbarcacoesByProprietario(proprietarioId)
                _uiState.update { it.copy(embarcacoes = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Erro ao carregar embarcações") }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
} 