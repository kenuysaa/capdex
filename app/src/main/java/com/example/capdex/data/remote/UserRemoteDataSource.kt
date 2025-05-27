package com.example.capdex.data.remote

import com.example.capdex.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<User?> {
        return try {
            val document = usersCollection.document(uid).get().await()
            val user = document.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}