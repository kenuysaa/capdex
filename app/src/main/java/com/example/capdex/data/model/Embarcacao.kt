package com.example.capdex.data.model

// Garanta que sua data class tenha todos estes campos, especialmente 'imagemUrl'
data class Embarcacao(
    val idEmbarcacao: String = "",
    val nomeEmbarcacao: String = "",
    val cnpj: String = "",
    val status: String = "",
    val imagemUrl: String = "", // ✅ Campo para a URL da imagem do Firebase Storage
    val proprietarioId: String = "",
    val nomeSetor: String = "",
    val senhaSetor: String = "",
    val pontoPartida: String = "",
    val pontoChegada: String = ""
    // Adicione outros campos se necessário, mas não remova estes
)
