package com.example.capdex.data.repository

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<AuthResult>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<AuthResult>
    fun signOut()
}