package com.example.capdex.ui.embarcacao

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

// Estado da UI para o cadastro de encomenda
data class CadastroSetorEncomendaUiState(
    val encomenda: String = "",
    val img: String = "",
    val status: String = "",
    val embarcacaoId: String = "",
    val remetenteCpf: String = "",
    val destinatarioCpf: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class CadastroSetorEncomendaViewModel @Inject constructor(
    private val encRepository: EncRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CadastroSetorEncomendaUiState())
    val uiState: StateFlow<CadastroSetorEncomendaUiState> = _uiState

    fun onEncomendaChanged(value: String) {
        _uiState.update { it.copy(encomenda = value) }
    }
    fun onImgChanged(value: String) {
        _uiState.update { it.copy(img = value) }
    }
    fun onStatusChanged(value: String) {
        _uiState.update { it.copy(status = value) }
    }
    fun onEmbarcacaoIdChanged(value: String) {
        _uiState.update { it.copy(embarcacaoId = value) }
    }
    fun onRemetenteCpfChanged(value: String) {
        _uiState.update { it.copy(remetenteCpf = value) }
    }
    fun onDestinatarioCpfChanged(value: String) {
        _uiState.update { it.copy(destinatarioCpf = value) }
    }

    fun cadastrarEncomenda() {
        val state = _uiState.value
        if (state.encomenda.isBlank() || state.embarcacaoId.isBlank() || state.remetenteCpf.isBlank() || state.destinatarioCpf.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Preencha todos os campos obrigatórios.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null, success = false) }
        val encomenda = Encomenda(
            idEncomenda = gerarIdUnico(),
            encomenda = state.encomenda,
            img = state.img,
            status = state.status,
            embarcacaoId = state.embarcacaoId,
            remetenteCpf = state.remetenteCpf,
            destinatarioCpf = state.destinatarioCpf
        )
        viewModelScope.launch {
            try {
                encRepository.addEncomenda(encomenda)
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Erro ao cadastrar encomenda") }
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