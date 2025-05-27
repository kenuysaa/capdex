package com.example.capdex.data.repository

import com.example.capdex.data.remote.EmbarcacaoRemoteDataSource
import com.example.capdex.domain.model.Embarcacao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmbarcacaoRepositoryImpl @Inject constructor(
    private val remoteDataSource: EmbarcacaoRemoteDataSource
) : EmbarcacaoRepository {
    override suspend fun salvarEmbarcacao(embarcacao: Embarcacao, proprietarioId: String): Result<Unit> {
        return remoteDataSource.adicionarEmbarcacao(embarcacao, proprietarioId)
    }

    override fun getEmbarcacoesDoProprietario(proprietarioId: String): Flow<Result<List<Embarcacao>>> {
        return remoteDataSource.obterEmbarcacoesDoProprietario(proprietarioId)
    }

    override fun getEmbarcacoes(): Flow<Result<List<Embarcacao>>> {
        // Lógica para obter todas as embarcações, independentemente do proprietárioreturn remoteDataSource.obterEmbarcacoes()
        return TODO("Provide the return value")
    }
}