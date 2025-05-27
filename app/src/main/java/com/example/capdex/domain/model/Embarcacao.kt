package com.example.capdex.domain.model

data class Embarcacao(
    val id: String = "",
    val nome: String = "",
    val proprietarioNome: String = "",
    val proprietarioId: String = "", // Adicionado o ID do proprietário
    val cnpj: String = "",
    val telefone: String = "",
    // val localizacao: ...
    // val statusDisponibilidade: ...
)