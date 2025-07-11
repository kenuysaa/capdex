package com.example.capdex.ui.embarcacao

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.model.Embarcacao
import com.example.capdex.ui.telas.EmbarcacaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI para esta tela específica
data class CadastroEmbarcacaoUiState(
    val nomeEmbarcacao: String = "",
    val cnpj: String = "",
    val nomeSetor: String = "",
    val senhaSetor: String = "", // Nome corrigido para consistência
    val imageUri: Uri? = null,
    val cadastroSucesso: Boolean = false,
    val erro: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class CadastroEmbarcacaoViewModel @Inject constructor(
    private val repository: EmbarcacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroEmbarcacaoUiState())
    val uiState: StateFlow<CadastroEmbarcacaoUiState> = _uiState

    // Funções para a UI notificar mudanças
    fun onNomeChange(nome: String) {
        _uiState.update { it.copy(nomeEmbarcacao = nome) }
    }
    fun onCnpjChange(cnpj: String) {
        _uiState.update { it.copy(cnpj = cnpj) }
    }
    fun onNomeSetorChange(nomeSetor: String) {
        _uiState.update { it.copy(nomeSetor = nomeSetor) }
    }
    fun onSenhaChange(senha: String) {
        _uiState.update { it.copy(senhaSetor = senha) } // Atualiza o campo correto
    }
    fun onImageUriChange(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    // Função de salvar agora usa os dados do próprio estado
    fun salvarEmbarcacao() {
        Log.d("CadastroViewModel", "salvarEmbarcacao() foi chamado")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val estadoAtual = _uiState.value
            val id = repository.getNovoId()

            try {
                // Faz o upload da imagem e obtém a URL
                val urlDaImagem = estadoAtual.imageUri?.let {
                    repository.uploadImagemEmbarcacao(it, id)
                } ?: throw Exception("Imagem é obrigatória")

                // Cria o objeto com os dados do estado
                val novaEmbarcacao = Embarcacao(
                    idEmbarcacao = id,
                    nomeEmbarcacao = estadoAtual.nomeEmbarcacao,
                    cnpj = estadoAtual.cnpj,
                    status = "Disponível",
                    imagemUrl = urlDaImagem,
                    // ✅ Campos de setor e senha agora são incluídos
                    nomeSetor = estadoAtual.nomeSetor,
                    senhaSetor = estadoAtual.senhaSetor,
                    proprietarioId = "proprietario123"
                )

                // Salva no banco de dados
                val sucesso = repository.addEmbarcacao(novaEmbarcacao)
                Log.d("CadastroViewModel", "Resultado do addEmbarcacao: $sucesso")

                if (sucesso) {
                    _uiState.update { it.copy(isLoading = false, cadastroSucesso = true) }
                } else {
                    throw Exception("Falha ao salvar a embarcação no banco de dados.")
                }
            } catch (e: Exception) {
                Log.e("CadastroViewModel", "Erro ao salvar: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, erro = e.message ?: "Ocorreu um erro desconhecido.") }
            }
        }
    }

    fun resetarEstado() {
        _uiState.value = CadastroEmbarcacaoUiState()
    }
}
