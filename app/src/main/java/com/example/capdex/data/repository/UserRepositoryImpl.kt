package com.example.capdex.data.repository

import com.example.capdex.data.remote.UserRemoteDataSource
import com.example.capdex.domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun saveUser(user: User): Result<Unit> {
        return remoteDataSource.saveUser(user)
    }

    override suspend fun getUser(uid: String): Result<User?> {
        return remoteDataSource.getUser(uid) // Chamando a função correspondente no DataSource
    }
}