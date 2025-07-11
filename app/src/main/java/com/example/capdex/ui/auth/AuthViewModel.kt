package com.example.capdex.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.repository.AuthRepository
import com.example.capdex.data.repository.UserRepository
import com.example.capdex.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val nomeCompleto: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val userUid: String? = null,
    val isDono: Boolean? = null // ✅ 1. Adicionado para saber o tipo de usuário
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    internal val authRepository: AuthRepository,
    private val userRepository: UserRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    // ✅ 2. Adicionado para gerenciar a seleção na UI de Cadastro
    private val _tipoDeConta = MutableStateFlow("Cliente")
    val tipoDeConta: StateFlow<String> = _tipoDeConta.asStateFlow()

    fun onTipoDeContaChange(novoTipo: String) {
        _tipoDeConta.value = novoTipo
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onNomeCompletoChanged(nomeCompleto: String) {
        _uiState.update { it.copy(nomeCompleto = nomeCompleto) }
    }

    fun registerUser(userType: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, userUid = null, isDono = null) }
        viewModelScope.launch {
            val isProprietario = userType == "Dono de Embarcação" // Use o mesmo texto da UI

            try {
                val userUid = authRepository.registerUser(uiState.value.email, uiState.value.password)
                if (userUid != null) {
                    val usuario = Usuario(
                        idUser = userUid,
                        email = uiState.value.email,
                        nome = uiState.value.nomeCompleto,
                        proprietario = isProprietario,
                        cpf = ""
                    )
                    userRepository.addUsuario(usuario)
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            successMessage = "Cadastro realizado com sucesso!",
                            userUid = userUid,
                            isDono = isProprietario // ✅ 3. Informa o tipo de usuário no estado
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Falha no cadastro. Verifique os dados e tente novamente.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro inesperado durante o cadastro: ${e.localizedMessage}") }
            }
        }
    }

    fun loginUser() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, userUid = null, isDono = null) }
        viewModelScope.launch {
            try {
                val userUid = authRepository.loginUser(uiState.value.email, uiState.value.password)
                if (userUid != null) {
                    // ✅ 4. Lógica para verificar o tipo de usuário após o login
                    val usuario = userRepository.getUsuario(userUid) // Busca os dados do usuário
                    val isProprietario = usuario?.proprietario ?: false // Verifica se é proprietário

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            successMessage = "Login realizado com sucesso!",
                            userUid = userUid,
                            isDono = isProprietario // Informa o tipo de usuário no estado
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Falha no login. Verifique seu e-mail e senha.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro inesperado durante o login: ${e.localizedMessage}") }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _uiState.update { AuthUiState() } // Reseta para o estado inicial
    }
}