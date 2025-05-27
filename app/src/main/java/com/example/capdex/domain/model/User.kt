package com.example.capdex.domain.model

data class User(
    val uid: String = "",
    val email: String? = null,
    val userType: String = "comum",
    val displayName: String? = null,
)
