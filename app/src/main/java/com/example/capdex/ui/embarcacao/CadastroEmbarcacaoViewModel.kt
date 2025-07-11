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

// Estado da UI para o cadastro de embarcação
data class CadastroEmbarcacaoUiState(
    val nomeEmbarcacao: String = "",
    val cnpj: String = "",
    val imagemResId: Int = 0,
    val nomeSetor: String = "",
    val senhaSetor: String = "",
    val pontoPartida: String = "",
    val pontoChegada: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class CadastroEmbarcacaoViewModel @Inject constructor(
    private val embarRepository: EmbarRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CadastroEmbarcacaoUiState())
    val uiState: StateFlow<CadastroEmbarcacaoUiState> = _uiState

    fun onNomeEmbarcacaoChanged(value: String) {
        _uiState.update { it.copy(nomeEmbarcacao = value) }
    }
    fun onCnpjChanged(value: String) {
        _uiState.update { it.copy(cnpj = value) }
    }
    fun onImagemResIdChanged(value: Int) {
        _uiState.update { it.copy(imagemResId = value) }
    }

    fun onNomeSetorChanged(value: String) {
        _uiState.update { it.copy(nomeSetor = value) }
    }
    fun onSenhaSetorChanged(value: String) {
        _uiState.update { it.copy(senhaSetor = value) }
    }
    fun onPontoPartidaChanged(value: String) {
        _uiState.update { it.copy(pontoPartida = value) }
    }
    fun onPontoChegadaChanged(value: String) {
        _uiState.update { it.copy(pontoChegada = value) }
    }

    fun cadastrarEmbarcacao(proprietarioId: String) {
        val state = _uiState.value
        if (state.nomeEmbarcacao.isBlank() || state.cnpj.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Preencha todos os campos obrigatórios.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null, success = false) }
        val embarcacao = Embarcacao(
            idEmbarcacao = gerarIdUnico(),
            nomeEmbarcacao = state.nomeEmbarcacao,
            cnpj = state.cnpj,
            imagemResId = state.imagemResId,
            nomeSetor = state.nomeSetor,
            senhaSetor = state.senhaSetor,
            pontoPartida = state.pontoPartida,
            pontoChegada = state.pontoChegada,
            proprietarioId = proprietarioId,
            status = "Disponível"
        )
        viewModelScope.launch {
            try {
                embarRepository.addEmbarcacao(embarcacao)
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Erro ao cadastrar embarcação") }
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }

    private fun gerarIdUnico(): String {
        // Pode ser substituído por um gerador de ID mais robusto se necessário
        return System.currentTimeMillis().toString()
    }
} 