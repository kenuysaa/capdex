package com.example.capdex.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): String?
    suspend fun loginUser(email: String, password: String): String?
    fun getCurrentUserUid(): String?
    fun signOut()
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun registerUser(email: String, password: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            // Logar o erro ou lançar uma exceção mais específica
            null
        }
    }

    override suspend fun loginUser(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            // Logar o erro ou lançar uma exceção mais específica
            null
        }
    }

    override fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    override fun signOut() {
        auth.signOut()
    }
}