package com.example.capdex.ui.embarcacao // Ou o pacote onde ele está

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.model.Embarcacao
import com.example.capdex.data.repository.EmbarcacaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI para sabermos se o cadastro foi bem-sucedido
data class CadastroUiState(
    val sucesso: Boolean = false,
    val erro: String? = null
)

@HiltViewModel
class CadastroEmbarcacaoViewModel @Inject constructor(
    private val repository: EmbarcacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState = _uiState.asStateFlow()

    fun salvarEmbarcacao(
        nomeEmbarcacao: String,
        cnpj: String,
        nomeSetorEncomenda: String,
        senhaSetorEncomenda: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            // TODO: Adicionar validações (campos vazios, senhas não conferem, etc.)

            // Exemplo de como criar o objeto e chamar o repositório
            val novaEmbarcacao = Embarcacao(
                // O ID pode ser gerado pelo Firebase ou de outra forma
                idEmbarcacao = repository.getNovoId(),
                nomeEmbarcacao = nomeEmbarcacao,
                cnpj = cnpj,
                status = "Disponível", // Status inicial padrão
                // Adicione os novos campos ao seu modelo de dados 'Embarcacao'
                // nomeSetorEncomenda = nomeSetorEncomenda,
                // senhaSetorEncomenda = senhaSetorEncomenda,
                imagemUrl = "" // A URL da imagem será preenchida após o upload
            )

            // Primeiro, faz o upload da imagem (se houver) e pega a URL
            val urlDaImagem = imageUri?.let { repository.uploadImagemEmbarcacao(it, novaEmbarcacao.idEmbarcacao) }

            // Atualiza o objeto com a URL da imagem e salva no banco de dados
            val resultado = repository.addEmbarcacao(novaEmbarcacao.copy(imagemUrl = urlDaImagem ?: ""))

            if (resultado) {
                _uiState.value = CadastroUiState(sucesso = true)
            } else {
                _uiState.value = CadastroUiState(erro = "Falha ao salvar a embarcação.")
            }
        }
    }

    // Função para resetar o estado após a navegação
    fun resetarEstado() {
        _uiState.value = CadastroUiState()
    }
}