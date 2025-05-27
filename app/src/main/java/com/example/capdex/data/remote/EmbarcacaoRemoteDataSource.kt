package com.example.capdex.data.remote

import com.example.capdex.domain.model.Embarcacao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmbarcacaoRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val embarcacoesCollection = firestore.collection("embarcacoes")

    suspend fun adicionarEmbarcacao(embarcacao: Embarcacao, proprietarioId: String): Result<Unit> {
        val embarcacaoComProprietarioId = embarcacao.copy(proprietarioId = proprietarioId)
        return try {
            embarcacoesCollection.add(embarcacaoComProprietarioId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun obterEmbarcacoesDoProprietario(proprietarioId: String): Flow<Result<List<Embarcacao>>> {
        return kotlinx.coroutines.flow.callbackFlow {
            val listener = embarcacoesCollection
                .whereEqualTo("proprietarioId", proprietarioId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(error)).isSuccess
                        close()
                        return@addSnapshotListener
                    }
                    val embarcacoes = snapshot?.documents?.mapNotNull { it.toObject(Embarcacao::class.java)?.copy(id = it.id) } ?: emptyList()
                    trySend(Result.success(embarcacoes)).isSuccess
                }
            awaitClose { listener.remove() }
        }
    }
}