package com.example.capdex.di

import android.content.Context
import com.example.capdex.data.repository.AuthRepository
import com.example.capdex.data.repository.AuthRepositoryImpl
import com.example.capdex.data.repository.EmbarRepository
import com.example.capdex.data.repository.EmbarRepositoryImpl
import com.example.capdex.data.repository.EncRepository
import com.example.capdex.data.repository.EncRepositoryImpl
import com.example.capdex.data.repository.UserRepository
import com.example.capdex.data.repository.UserRepositoryImpl
import com.example.capdex.data.location.LocationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext // Importar para Context
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideEncRepository(db: FirebaseFirestore): EncRepository {
        return EncRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideEmbarRepository(db: FirebaseFirestore): EmbarRepository {
        return EmbarRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideLocationService(
        @ApplicationContext context: Context,
        embarRepository: EmbarRepository
    ): LocationService {
        return LocationService(context, embarRepository)
    }
}