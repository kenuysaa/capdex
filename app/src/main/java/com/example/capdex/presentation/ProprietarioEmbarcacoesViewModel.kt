package com.example.capdex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.repository.EmbarcacaoRepository
import com.example.capdex.domain.model.Embarcacao
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OwnerVesselsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val vessels: List<Embarcacao> = emptyList()
)

@HiltViewModel
class OwnerVesselsViewModel @Inject constructor(
    private val embarcacaoRepository: EmbarcacaoRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerVesselsUiState())
    val uiState: StateFlow<OwnerVesselsUiState> = _uiState

    init {
        loadOwnerVessels()
    }

    private fun loadOwnerVessels() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            viewModelScope.launch {
                embarcacaoRepository.getEmbarcacoesDoProprietario(currentUserId)
                    .collect { result ->
                        result.onSuccess { vessels ->
                            _uiState.update { it.copy(isLoading = false, vessels = vessels) }
                        }
                        result.onFailure { error ->
                            _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage) }
                        }
                    }
            }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Usuário não autenticado.") }
        }
    }
}