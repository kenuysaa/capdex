package com.example.capdex.di

import com.example.capdex.data.repository.AuthRepository
import com.example.capdex.data.repository.AuthRepositoryImpl
import com.example.capdex.data.repository.EncRepository
import com.example.capdex.data.repository.EncRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    // Seu binding já existente
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    // Novo binding para o EncRepository
    @Binds
    @Singleton
    abstract fun bindEncRepository(
        encRepositoryImpl: EncRepositoryImpl
    ): EncRepository
}


