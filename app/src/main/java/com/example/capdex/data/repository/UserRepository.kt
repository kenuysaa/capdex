package com.example.capdex.data.repository

import android.util.Log
import com.example.capdex.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserRepository {
    suspend fun addUsuario(usuario: Usuario)
    suspend fun getUsuario(userId: String): Usuario?
    suspend fun updateUsuario(usuario: Usuario)
    suspend fun deleteUsuario(userId: String)
    fun observeUsuario(userId: String): Flow<Usuario?>
}

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRepository {
    companion object {
        private const val USUARIO_COLLECTION = "usuario"
    }

    override suspend fun addUsuario(usuario: Usuario) {
        db.collection(USUARIO_COLLECTION)
            .document(usuario.idUser)
            .set(usuario)
            .await()
    }

    override suspend fun getUsuario(userId: String): Usuario? {
        return try {
            val document = db.collection(USUARIO_COLLECTION)
                .document(userId)
                .get()
                .await()
            document.toObject(Usuario::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro ao obter usuário $userId", e)
            null
        }
    }

    override suspend fun updateUsuario(usuario: Usuario) {
        db.collection(USUARIO_COLLECTION)
            .document(usuario.idUser)
            .set(usuario)
            .await()
    }

    override suspend fun deleteUsuario(userId: String) {
        db.collection(USUARIO_COLLECTION)
            .document(userId)
            .delete()
            .await()
    }

    override fun observeUsuario(userId: String): Flow<Usuario?> = callbackFlow {
        val docRef = db.collection(USUARIO_COLLECTION).document(userId)
        val subscription = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("UserRepository", "Erro na observação do usuário $userId", e)
                close(e) // Fecha o Flow em caso de erro
                return@addSnapshotListener
            }
            trySend(snapshot?.toObject(Usuario::class.java))
        }
        awaitClose { subscription.remove() }
    }
}