package com.example.capdex.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.model.Usuario
import com.example.capdex.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // For one-time operations like add, update, delete
    private val _addUpdateDeleteResult = MutableStateFlow<Result<Unit>?>(null)
    val addUpdateDeleteResult: StateFlow<Result<Unit>?> = _addUpdateDeleteResult

    // For fetching a single user once
    private val _fetchedUser = MutableStateFlow<Usuario?>(null)
    val fetchedUser: StateFlow<Usuario?> = _fetchedUser

    // For real-time observation of a user
    private val _observedUser = MutableStateFlow<Usuario?>(null)
    val observedUser: StateFlow<Usuario?> = _observedUser.asStateFlow()

    fun addUsuario(usuario: Usuario) {
        viewModelScope.launch {
            _addUpdateDeleteResult.value = try {
                userRepository.addUsuario(usuario)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun getUsuario(userId: String) {
        viewModelScope.launch {
            _fetchedUser.value = try {
                userRepository.getUsuario(userId)
            } catch (e: Exception) {
                // You might want to expose this error via a separate StateFlow
                // or log it more prominently for debugging.
                null
            }
        }
    }

    fun updateUsuario(usuario: Usuario) {
        viewModelScope.launch {
            _addUpdateDeleteResult.value = try {
                userRepository.updateUsuario(usuario)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun deleteUsuario(userId: String) {
        viewModelScope.launch {
            _addUpdateDeleteResult.value = try {
                userRepository.deleteUsuario(userId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Starts observing real-time changes for a specific user.
     * The `observedUser` StateFlow will be updated whenever the user's data changes in Firestore.
     */
    fun observeUser(userId: String) {
        viewModelScope.launch {
            userRepository.observeUsuario(userId).collectLatest { usuario ->
                _observedUser.value = usuario
            }
        }
    }

    /**
     * Resets the result of add/update/delete operations after it has been consumed by the UI.
     * Prevents re-triggering UI effects on recomposition.
     */
    fun resetAddUpdateDeleteResult() {
        _addUpdateDeleteResult.value = null
    }
}