package com.example.capdex.presentation

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
    val isLoading: Boolean = true
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository // Precisamos para buscar os dados do usuário
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState

    init {
        loadUserType()
    }

    private fun loadUserType() {
        _uiState.update { it.copy(isLoading = true) }
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                userRepository.getUser(currentUser.uid) // Assumindo que você tem essa função
                    .onSuccess { user ->
                        _uiState.update { it.copy(isProprietario = user?.userType == "proprietario", isLoading = false) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                        // Lidar com o erro ao buscar o tipo de usuário
                    }
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}