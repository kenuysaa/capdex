package com.example.capdex.data.model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PacotesViewModel @Inject constructor() : ViewModel() {

    // Dados fictícios só para visualização
    private val _pacotesEnviados = MutableStateFlow(
        listOf(
            Pacote(1, "Maria Cecilia Brito", "Barco Correa Filho", "Manaus", "15h:43m"),
            Pacote(2, "João Alfredo", "Barco Manoel", "Itacoatiara", "10h:12m")
        )
    )
    val pacotesEnviados: StateFlow<List<Pacote>> = _pacotesEnviados

    private val _pacotesRecebidos = MutableStateFlow(
        listOf(
            Pacote(3, "Carlos Eduardo", "Lancha Rápida", "Codajás", "3h:20m"),
            Pacote(4, "Fernanda Souza", "Ferry Boat", "Coari", "8h:15m")
        )
    )
    val pacotesRecebidos: StateFlow<List<Pacote>> = _pacotesRecebidos
}

