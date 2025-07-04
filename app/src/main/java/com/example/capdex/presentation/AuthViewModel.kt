package com.example.capdex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.repository.AuthRepository
import com.example.capdex.data.repository.UserRepository
import com.example.capdex.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val userUid: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

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
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, userUid = null) }
        viewModelScope.launch {
            // Converte a string userType para o booleano proprietario
            val isProprietario = userType == "proprietario"

            authRepository.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
                .onSuccess { authResult ->
                    authResult.user?.let { firebaseUser ->
                        val usuario = Usuario(
                            idUser = firebaseUser.uid,
                            email = uiState.value.email,
                            nome = uiState.value.nomeCompleto,
                            proprietario = isProprietario,
                            cpf = "" // Assumindo que CPF não é coletado nesta etapa de registro
                        )
                        try {
                            // Chama o novo método addUsuario do UserRepository
                            userRepository.addUsuario(usuario)
                            _uiState.update { currentState ->
                                currentState.copy(isLoading = false, successMessage = "Cadastro realizado com sucesso!", userUid = firebaseUser.uid)
                            }
                        } catch (e: Exception) {
                            _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao salvar dados do usuário: ${e.localizedMessage}") }
                        }
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                }
        }
    }

    fun loginUser() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null, userUid = null) }
        viewModelScope.launch {
            authRepository.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
                .onSuccess { authResult ->
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, successMessage = "Login realizado com sucesso!", userUid = authResult.user?.uid)
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _uiState.update { AuthUiState() } // Resetar o estado ao sair
    }
}