package com.example.capdex.di

import com.example.capdex.data.repository.EmbarRepository
import com.example.capdex.data.repository.EmbarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EmbarRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmbarRepository(
        impl: EmbarRepositoryImpl
    ): EmbarRepository
}


