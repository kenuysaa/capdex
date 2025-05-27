package com.example.capdex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.repository.EmbarcacaoRepository
import com.example.capdex.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmbarcacaoRegistrationUiState(
    val nomeEmbarcacao: String = "",
    val cnpj: String = "",
    val telefone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isEmbarcacaoRegistrada: Boolean = false
)

@HiltViewModel
class EmbarcacaoRegistrationViewModel @Inject constructor(
    private val embarcacaoRepository: EmbarcacaoRepository,
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmbarcacaoRegistrationUiState())
    val uiState: StateFlow<EmbarcacaoRegistrationUiState> = _uiState

    fun onNomeEmbarcacaoChanged(name: String) {
        _uiState.update { it.copy(nomeEmbarcacao = name) }
    }

    fun onCnpjChanged(cnpj: String) {
        _uiState.update { it.copy(cnpj = cnpj) }
    }

    fun onTelefoneChanged(phone: String) {
        _uiState.update { it.copy(telefone = phone) }
    }

    fun registerEmbarcacao() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, isEmbarcacaoRegistrada = false) }
        val currentUser = firebaseAuth.currentUser
        val proprietarioId = currentUser?.uid

        if (proprietarioId != null) {
            viewModelScope.launch {
                userRepository.getUser(proprietarioId)
                    .onSuccess { user ->
                        val proprietarioNome = user?.displayName ?: "" // Pega o nome do usuário

                        val embarcacao = com.example.capdex.domain.model.Embarcacao(
                            nome = uiState.value.nomeEmbarcacao,
                            proprietarioNome = proprietarioNome,
                            proprietarioId = proprietarioId,
                            cnpj = uiState.value.cnpj,
                            telefone = uiState.value.telefone
                            // localização e status serão adicionados posteriormente
                        )

                        embarcacaoRepository.salvarEmbarcacao(embarcacao, proprietarioId)
                            .onSuccess {
                                _uiState.update {
                                    it.copy(isLoading = false, successMessage = "Embarcação cadastrada com sucesso!", isEmbarcacaoRegistrada = true)
                                }
                            }
                            .onFailure { e ->
                                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao cadastrar embarcação: ${e.localizedMessage}") }
                            }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao buscar informações do proprietário: ${e.localizedMessage}") }
                    }
            }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Usuário não autenticado.") }
        }
    }
}