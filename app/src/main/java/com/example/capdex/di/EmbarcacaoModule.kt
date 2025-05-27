package com.example.capdex.di

import com.example.capdex.data.remote.EmbarcacaoRemoteDataSource
import com.example.capdex.data.repository.EmbarcacaoRepository
import com.example.capdex.data.repository.EmbarcacaoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmbarcacaoModule {

    @Provides
    @Singleton
    fun provideEmbarcacaoRemoteDataSource(firestore: com.google.firebase.firestore.FirebaseFirestore): EmbarcacaoRemoteDataSource {
        return EmbarcacaoRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideEmbarcacaoRepository(remoteDataSource: EmbarcacaoRemoteDataSource): EmbarcacaoRepository {
        return EmbarcacaoRepositoryImpl(remoteDataSource)
    }
}