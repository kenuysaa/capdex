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
import com.example.capdex.data.repository.AuthRepository
import com.example.capdex.data.repository.EmbarRepository
import com.example.capdex.data.model.Embarcacao

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
    private val encRepository: EncRepository,
    private val embarRepository: EmbarRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CadastroEncomendaUiState())
    val uiState: StateFlow<CadastroEncomendaUiState> = _uiState

    private val _embarcacoes = MutableStateFlow<List<Embarcacao>>(emptyList())
    val embarcacoes: StateFlow<List<Embarcacao>> = _embarcacoes

    init {
        val proprietarioId = authRepository.getCurrentUserUid()
        if (!proprietarioId.isNullOrBlank()) {
            viewModelScope.launch {
                val lista = embarRepository.getEmbarcacoesByProprietario(proprietarioId)
                _embarcacoes.value = lista
            }
        }
    }

    fun onEncomendaChange(valor: String) {
        _uiState.value = _uiState.value.copy(encomenda = valor)
    }
    fun onRemetenteCpfChange(valor: String) {
        _uiState.value = _uiState.value.copy(remetenteCpf = valor)
    }
    fun onDestinatarioCpfChange(valor: String) {
        _uiState.value = _uiState.value.copy(destinatarioCpf = valor)
    }
    fun onEmbarcacaoSelecionada(embarcacaoId: String) {
        _uiState.value = _uiState.value.copy(embarcacaoId = embarcacaoId)
    }
    fun onStatusChange(valor: String) {
        _uiState.value = _uiState.value.copy(status = valor)
    }
    fun resetarEstado() {
        _uiState.value = CadastroEncomendaUiState()
    }

    fun salvarEncomenda() {
        // Validação inicial
        if (_uiState.value.encomenda.isBlank() || _uiState.value.remetenteCpf.isBlank() || _uiState.value.destinatarioCpf.isBlank() || _uiState.value.embarcacaoId.isBlank()) {
            _uiState.value = _uiState.value.copy(erro = "Preencha todos os campos obrigatórios.")
            return
        }

        // Inicia o processo de salvamento
        _uiState.value = _uiState.value.copy(isLoading = true, erro = null, cadastroSucesso = false)

        // Cria o objeto da encomenda a partir do estado ATUAL
        val estadoAtual = _uiState.value
        val novaEncomenda = Encomenda(
            idEncomenda = gerarIdUnico(),
            encomenda = estadoAtual.encomenda,
            img = "", // Pode ser implementado upload de imagem depois
            status = estadoAtual.status,
            embarcacaoId = estadoAtual.embarcacaoId,
            remetenteCpf = estadoAtual.remetenteCpf,
            destinatarioCpf = estadoAtual.destinatarioCpf
        )

        viewModelScope.launch {
            try {
                encRepository.addEncomenda(novaEncomenda)
                // CORREÇÃO: Atualiza a partir do estado mais recente
                _uiState.value = _uiState.value.copy(isLoading = false, cadastroSucesso = true)
            } catch (e: Exception) {
                // CORREÇÃO: Atualiza a partir do estado mais recente
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    erro = e.localizedMessage ?: "Erro ao cadastrar encomenda"
                )
            }
        }
    }


    private fun gerarIdUnico(): String {
        return System.currentTimeMillis().toString()
    }
}
