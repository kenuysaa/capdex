package com.example.capdex.ui.encomenda

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.capdex.data.model.Encomenda
import com.example.capdex.data.repository.EncRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Deixando este arquivo limpo para remover os conflitos.
// A lógica de cadastro de encomenda pode ser implementada aqui no futuro.

data class CadastroEncomendaUiState(
    val encomenda: String = "",
    val remetenteCpf: String = "",
    val destinatarioCpf: String = "",
    val embarcacaoId: String = "",
    val status: String = "Pendente",
    val isLoading: Boolean = false,
    val cadastroSucesso: Boolean = false,
    val erro: String? = null
)

@HiltViewModel
class CadastroEncomendaViewModel @Inject constructor(
    private val encRepository: EncRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CadastroEncomendaUiState())
    val uiState: StateFlow<CadastroEncomendaUiState> = _uiState

    fun onEncomendaChange(valor: String) {
        _uiState.value = _uiState.value.copy(encomenda = valor)
    }
    fun onRemetenteCpfChange(valor: String) {
        _uiState.value = _uiState.value.copy(remetenteCpf = valor)
    }
    fun onDestinatarioCpfChange(valor: String) {
        _uiState.value = _uiState.value.copy(destinatarioCpf = valor)
    }
    fun onEmbarcacaoIdChange(valor: String) {
        _uiState.value = _uiState.value.copy(embarcacaoId = valor)
    }
    fun onStatusChange(valor: String) {
        _uiState.value = _uiState.value.copy(status = valor)
    }
    fun resetarEstado() {
        _uiState.value = CadastroEncomendaUiState()
    }

    fun salvarEncomenda() {
        val state = _uiState.value
        if (state.encomenda.isBlank() || state.remetenteCpf.isBlank() || state.destinatarioCpf.isBlank() || state.embarcacaoId.isBlank()) {
            _uiState.value = state.copy(erro = "Preencha todos os campos obrigatórios.", isLoading = false)
            return
        }
        _uiState.value = state.copy(isLoading = true, erro = null, cadastroSucesso = false)
        val novaEncomenda = Encomenda(
            idEncomenda = gerarIdUnico(),
            encomenda = state.encomenda,
            img = "", // Pode ser implementado upload de imagem depois
            status = state.status,
            embarcacaoId = state.embarcacaoId,
            remetenteCpf = state.remetenteCpf,
            destinatarioCpf = state.destinatarioCpf
        )
        viewModelScope.launch {
            try {
                encRepository.addEncomenda(novaEncomenda)
                _uiState.value = state.copy(isLoading = false, cadastroSucesso = true)
            } catch (e: Exception) {
                _uiState.value = state.copy(isLoading = false, erro = e.localizedMessage ?: "Erro ao cadastrar encomenda")
            }
        }
    }

    private fun gerarIdUnico(): String {
        return System.currentTimeMillis().toString()
    }
}
