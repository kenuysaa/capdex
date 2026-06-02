package com.example.capdex.data.model

data class Encomenda(
    val idEncomenda: String = "",
    val encomenda: String = "",
    val img: String = "", // URL da imagem
    val status: String = "",
    val embarcacaoId: String = "",
    val remetenteCpf: String = "",
    val destinatarioCpf: String = ""
)
