package com.example.capdex.ui.telas

import android.net.Uri
import com.example.capdex.data.model.Embarcacao
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Garante que só haverá uma instância deste repositório no app
class EmbarcacaoRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val embarcacaoCollection = firestore.collection("embarcacoes")

    /**
     * Gera um novo ID único para uma futura embarcação.
     */
    fun getNovoId(): String {
        return embarcacaoCollection.document().id
    }

    /**
     * Salva os dados de uma nova embarcação no banco de dados Firestore.
     */
    suspend fun addEmbarcacao(embarcacao: Embarcacao): Boolean {
        return try {
            embarcacaoCollection.document(embarcacao.idEmbarcacao).set(embarcacao).await()
            true
        } catch (e: Exception) {
            // Log do erro (opcional)
            e.printStackTrace()
            false
        }
    }

    /**
     * Faz o upload de uma imagem para o Firebase Storage e retorna a URL de download.
     */
    suspend fun uploadImagemEmbarcacao(imageUri: Uri, embarcacaoId: String): String? {
        return try {
            val storageRef = storage.reference.child("imagens_embarcacoes/$embarcacaoId.jpg")
            storageRef.putFile(imageUri).await() // Envia o arquivo
            storageRef.downloadUrl.await().toString() // Pega a URL pública da imagem
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Você pode adicionar outras funções aqui depois, como:
    // suspend fun getMinhasEmbarcacoes(donoId: String): List<Embarcacao> { ... }
    // suspend fun deletarEmbarcacao(id: String) { ... }
}