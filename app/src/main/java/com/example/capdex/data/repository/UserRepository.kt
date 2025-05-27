package com.example.capdex.data.repository

import com.example.capdex.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun getUser(uid: String): Result<User?>
}