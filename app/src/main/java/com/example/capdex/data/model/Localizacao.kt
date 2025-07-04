package com.example.capdex.data.model

data class Localizacao(
    val accuracy: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val speed: Double = 0.0,
    val timestamp: Long = 0L
)
