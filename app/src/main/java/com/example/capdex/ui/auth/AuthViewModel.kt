package com.example.capdex.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registrationResult = MutableStateFlow<Result<String?>?>(null)
    val registrationResult: StateFlow<Result<String?>?> = _registrationResult

    private val _loginResult = MutableStateFlow<Result<String?>?>(null)
    val loginResult: StateFlow<Result<String?>?> = _loginResult

    private val _currentUserUid = MutableStateFlow<String?>(null)
    val currentUserUid: StateFlow<String?> = _currentUserUid

    private val _signOutSuccess = MutableStateFlow<Boolean>(false)
    val signOutSuccess: StateFlow<Boolean> = _signOutSuccess

    init {
        // Immediately try to get the current user's UID when the ViewModel is created
        getCurrentUser()
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            _registrationResult.value = try {
                val uid = authRepository.registerUser(email, password)
                Result.success(uid)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = try {
                val uid = authRepository.loginUser(email, password)
                Result.success(uid)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun getCurrentUser() {
        _currentUserUid.value = authRepository.getCurrentUserUid()
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _signOutSuccess.value = true
            _currentUserUid.value = null
        }
    }

    fun resetRegistrationResult() {
        _registrationResult.value = null
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun resetSignOutSuccess() {
        _signOutSuccess.value = false
    }
}