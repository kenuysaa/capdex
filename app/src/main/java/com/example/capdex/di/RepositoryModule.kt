package com.example.capdex.di


import com.example.capdex.data.repository.UserRepository
import com.example.capdex.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    // Se você tiver outros repositórios com interfaces,
    // adicione os @Binds para eles aqui também.
    // Ex: abstract fun bindEmbarcacaoRepository(...): EmbarcacaoRepository
}