package com.example.capdex.data.model

data class Embarcacao(
    val idEmbarcacao: String = "",
    val nomeEmbarcacao: String = "",
    val cnpj: String = "",
    val imagemResId: Int = 0,
    val nomeSetor: String = "",
    val senhaSetor: String = "",
    val pontoPartida: String = "",
    val pontoChegada: String = "",
    val proprietarioId: String = ""
)
