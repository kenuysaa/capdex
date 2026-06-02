package com.example.capdex.ui.encomenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.model.Encomenda
import com.example.capdex.data.repository.EncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI para a lista de encomendas
data class ListaEncomendasUiState(
    val encomendas: List<Encomenda> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ListaEncomendasViewModel @Inject constructor(
    private val encRepository: EncRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListaEncomendasUiState())
    val uiState: StateFlow<ListaEncomendasUiState> = _uiState

    fun carregarEncomendasPorEmbarcacao(embarcacaoId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val lista = encRepository.getEncomendasByEmbarcacao(embarcacaoId)
                _uiState.update { it.copy(encomendas = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Erro ao carregar encomendas") }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
} 