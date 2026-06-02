package com.example.capdex.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainScreenUiState(
    val isProprietario: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState

    init {
        loadUserType()
    }

    private fun loadUserType() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    val usuario = userRepository.getUsuario(currentUser.uid)
                    _uiState.update { currentState ->
                        currentState.copy(
                            isProprietario = usuario?.proprietario ?: false,
                            isLoading = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "Erro ao carregar tipo de usuário: ${e.localizedMessage}"
                        )
                    }
                }
            }
        } else {
            _uiState.update { it.copy(isProprietario = false, isLoading = false) }
        }
    }
}