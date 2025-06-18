package com.example.capdex.data.repository

import android.util.Log
import com.example.capdex.data.model.Embarcacao
import com.example.capdex.data.model.Localizacao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface EmbarRepository {
    suspend fun addEmbarcacao(embarcacao: Embarcacao)
    suspend fun getEmbarcacao(embarcacaoId: String): Embarcacao?
    suspend fun updateEmbarcacao(embarcacao: Embarcacao)
    suspend fun deleteEmbarcacao(embarcacaoId: String)
    suspend fun getEmbarcacoesByProprietario(proprietarioId: String): List<Embarcacao>
    fun observeEmbarcacoesByProprietario(proprietarioId: String): Flow<List<Embarcacao>>

    suspend fun addLocalizacao(embarcacaoId: String, localizacao: Localizacao): String
    suspend fun getLatestLocalizacoes(embarcacaoId: String, limit: Long = 1): List<Localizacao>
    fun observeLatestLocalizacoes(embarcacaoId: String, limit: Long = 1): Flow<List<Localizacao>>
}

class EmbarRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : EmbarRepository {

    companion object {
        private const val EMBARCACAO_COLLECTION = "embarcacao"
        private const val LOCALIZACAO_SUBCOLLECTION = "loc"
    }

    override suspend fun addEmbarcacao(embarcacao: Embarcacao) {
        db.collection(EMBARCACAO_COLLECTION)
            .document(embarcacao.idEmbarcacao)
            .set(embarcacao)
            .await()
    }

    override suspend fun getEmbarcacao(embarcacaoId: String): Embarcacao? {
        return try {
            val document = db.collection(EMBARCACAO_COLLECTION)
                .document(embarcacaoId)
                .get()
                .await()
            document.toObject(Embarcacao::class.java)
        } catch (e: Exception) {
            Log.e("EmbarcacaoRepository", "Erro ao obter embarcação $embarcacaoId", e)
            null
        }
    }

    override suspend fun updateEmbarcacao(embarcacao: Embarcacao) {
        db.collection(EMBARCACAO_COLLECTION)
            .document(embarcacao.idEmbarcacao)
            .set(embarcacao)
            .await()
    }

    override suspend fun deleteEmbarcacao(embarcacaoId: String) {
        db.collection(EMBARCACAO_COLLECTION)
            .document(embarcacaoId)
            .delete()
            .await()
    }

    override suspend fun getEmbarcacoesByProprietario(proprietarioId: String): List<Embarcacao> {
        return try {
            val snapshot = db.collection(EMBARCACAO_COLLECTION)
                .whereEqualTo("proprietarioId", proprietarioId)
                .get()
                .await()
            snapshot.toObjects(Embarcacao::class.java)
        } catch (e: Exception) {
            // Adicionar log: Log.e("EmbarcacaoRepository", "Erro ao obter embarcações por proprietário", e)
            emptyList()
        }
    }

    override fun observeEmbarcacoesByProprietario(proprietarioId: String): Flow<List<Embarcacao>> = callbackFlow {
        val query = db.collection(EMBARCACAO_COLLECTION)
            .whereEqualTo("proprietarioId", proprietarioId)

        val subscription = query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("EmbarcacaoRepository", "Erro na observação de embarcações", e)
                close(e)
                return@addSnapshotListener
            }

            val embarcacoes = snapshot?.toObjects(Embarcacao::class.java) ?: emptyList()
            trySend(embarcacoes)
        }
        awaitClose { subscription.remove() }
    }

    // Implementações dos métodos da Subcoleção de Localização

    override suspend fun addLocalizacao(embarcacaoId: String, localizacao: Localizacao): String {
        return db.collection(EMBARCACAO_COLLECTION)
            .document(embarcacaoId)
            .collection(LOCALIZACAO_SUBCOLLECTION)
            .add(localizacao)
            .await()
            .id
    }

    override suspend fun getLatestLocalizacoes(embarcacaoId: String, limit: Long): List<Localizacao> {
        return try {
            val snapshot = db.collection(EMBARCACAO_COLLECTION)
                .document(embarcacaoId)
                .collection(LOCALIZACAO_SUBCOLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            snapshot.toObjects(Localizacao::class.java)
        } catch (e: Exception) {
            Log.e("EmbarcacaoRepository", "Erro ao obter últimas localizações", e)
            emptyList()
        }
    }

    override fun observeLatestLocalizacoes(embarcacaoId: String, limit: Long): Flow<List<Localizacao>> = callbackFlow {
        val query = db.collection(EMBARCACAO_COLLECTION)
            .document(embarcacaoId)
            .collection(LOCALIZACAO_SUBCOLLECTION)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit)

        val subscription = query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("EmbarcacaoRepository", "Erro na observação de localizações", e)
                close(e)
                return@addSnapshotListener
            }

            val localizacoes = snapshot?.toObjects(Localizacao::class.java) ?: emptyList()
            trySend(localizacoes)
        }
        awaitClose { subscription.remove() }
    }
}