package com.example.capdex.data.repository

import com.example.capdex.data.model.Encomenda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface EncRepository {
    suspend fun addEncomenda(encomenda: Encomenda)
    suspend fun getEncomenda(encomendaId: String): Encomenda?
    suspend fun updateEncomenda(encomenda: Encomenda)
    suspend fun deleteEncomenda(encomendaId: String)

    suspend fun getEncomendasByRemetente(cpfRemetente: String): List<Encomenda>
    suspend fun getEncomendasByDestinatario(cpfDestinatario: String): List<Encomenda>
    suspend fun getEncomendasByEmbarcacao(idEmbarcacao: String): List<Encomenda>
    suspend fun getEncomendasByStatus(status: String): List<Encomenda>

    fun observeEncomendasByRemetente(cpfRemetente: String): Flow<List<Encomenda>>
    fun observeEncomendasByDestinatario(cpfDestinatario: String): Flow<List<Encomenda>>
}

class EncRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : EncRepository {

    companion object {
        private const val ENCOMENDA_COLLECTION = "encomenda"
    }

    override suspend fun addEncomenda(encomenda: Encomenda) {
        db.collection(ENCOMENDA_COLLECTION)
            .document(encomenda.idEncomenda)
            .set(encomenda)
            .await()
    }

    override suspend fun getEncomenda(encomendaId: String): Encomenda? {
        return try {
            val document = db.collection(ENCOMENDA_COLLECTION)
                .document(encomendaId)
                .get()
                .await()
            document.toObject(Encomenda::class.java)
        } catch (e: Exception) {
            // Recomenda-se usar uma biblioteca de logging como Timber para produção
            // Timber.e(e, "Erro ao obter encomenda %s", encomendaId)
            null
        }
    }

    override suspend fun updateEncomenda(encomenda: Encomenda) {
        db.collection(ENCOMENDA_COLLECTION)
            .document(encomenda.idEncomenda)
            .set(encomenda)
            .await()
    }

    override suspend fun deleteEncomenda(encomendaId: String) {
        db.collection(ENCOMENDA_COLLECTION)
            .document(encomendaId)
            .delete()
            .await()
    }

    override suspend fun getEncomendasByRemetente(cpfRemetente: String): List<Encomenda> {
        return try {
            val snapshot = db.collection(ENCOMENDA_COLLECTION)
                .whereEqualTo("remetenteCpf", cpfRemetente)
                .get()
                .await()
            snapshot.toObjects(Encomenda::class.java)
        } catch (e: Exception) {
            // Timber.e(e, "Erro ao obter encomendas por remetente")
            emptyList()
        }
    }

    override suspend fun getEncomendasByDestinatario(cpfDestinatario: String): List<Encomenda> {
        return try {
            val snapshot = db.collection(ENCOMENDA_COLLECTION)
                .whereEqualTo("destinatarioCpf", cpfDestinatario)
                .get()
                .await()
            snapshot.toObjects(Encomenda::class.java)
        } catch (e: Exception) {
            // Timber.e(e, "Erro ao obter encomendas por destinatário")
            emptyList()
        }
    }

    override suspend fun getEncomendasByEmbarcacao(idEmbarcacao: String): List<Encomenda> {
        return try {
            val snapshot = db.collection(ENCOMENDA_COLLECTION)
                .whereEqualTo("embarcacaoId", idEmbarcacao)
                .get()
                .await()
            snapshot.toObjects(Encomenda::class.java)
        } catch (e: Exception) {
            // Timber.e(e, "Erro ao obter encomendas por embarcação")
            emptyList()
        }
    }

    override suspend fun getEncomendasByStatus(status: String): List<Encomenda> {
        return try {
            val snapshot = db.collection(ENCOMENDA_COLLECTION)
                .whereEqualTo("status", status)
                .get()
                .await()
            snapshot.toObjects(Encomenda::class.java)
        } catch (e: Exception) {
            // Timber.e(e, "Erro ao obter encomendas por status")
            emptyList()
        }
    }

    override fun observeEncomendasByRemetente(cpfRemetente: String): Flow<List<Encomenda>> = callbackFlow {
        val query = db.collection(ENCOMENDA_COLLECTION)
            .whereEqualTo("remetenteCpf", cpfRemetente)
            .orderBy("status", Query.Direction.ASCENDING)

        val subscription = query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Timber.e(e, "Erro na observação de encomendas por remetente")
                close(e)
                return@addSnapshotListener
            }

            val encomendas = snapshot?.toObjects(Encomenda::class.java) ?: emptyList()
            trySend(encomendas)
        }
        awaitClose { subscription.remove() }
    }

    override fun observeEncomendasByDestinatario(cpfDestinatario: String): Flow<List<Encomenda>> = callbackFlow {
        val query = db.collection(ENCOMENDA_COLLECTION)
            .whereEqualTo("destinatarioCpf", cpfDestinatario)
            .orderBy("status", Query.Direction.ASCENDING)

        val subscription = query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Timber.e(e, "Erro na observação de encomendas por destinatário")
                close(e)
                return@addSnapshotListener
            }

            val encomendas = snapshot?.toObjects(Encomenda::class.java) ?: emptyList()
            trySend(encomendas)
        }
        awaitClose { subscription.remove() }
    }
}