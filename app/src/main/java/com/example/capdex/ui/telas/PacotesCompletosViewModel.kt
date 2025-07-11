package com.example.capdex.ui.telas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capdex.data.model.Encomenda
import com.example.capdex.data.model.Pacote
import com.example.capdex.data.repository.EmbarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PacotesCompletosViewModel @Inject constructor(
    private val embarRepository: EmbarRepository
) : ViewModel() {
    private val _pacotes = MutableStateFlow<List<Pacote>>(emptyList())
    val pacotes: StateFlow<List<Pacote>> = _pacotes

    fun montarPacotes(encomendas: List<Encomenda>) {
        viewModelScope.launch {
            val pacotes = encomendas.map { encomenda ->
                val embarcacao = embarRepository.getEmbarcacao(encomenda.embarcacaoId)
                Pacote(
                    id = encomenda.idEncomenda.toIntOrNull() ?: encomenda.idEncomenda.hashCode(),
                    destinatario = encomenda.destinatarioCpf,
                    nomeBarco = embarcacao?.nomeEmbarcacao ?: "Desconhecido",
                    destino = "${embarcacao?.pontoPartida ?: "?"} -> ${embarcacao?.pontoChegada ?: "?"}",
                    tempoRestante = "Indefinido" // Não há campo real, pode ser calculado depois
                )
            }
            _pacotes.value = pacotes
        }
    }
} 