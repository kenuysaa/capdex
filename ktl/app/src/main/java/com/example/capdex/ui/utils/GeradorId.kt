package com.example.capdex.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object GeradorId {

    /**
     * Gera um ID baseado na data e hora atual (ano, mês, dia, hora, minuto, segundo, milissegundo),
     * com um sufixo aleatório para garantir unicidade em caso de múltiplas chamadas rápidas.
     * Formato: YYMMDDHHmmssSSS_XXXX (Ano, Mês, Dia, Hora, Minuto, Segundo, Milissegundo_SufixoAleatório)
     * Ex: 250608224357123_A1B2
     */

    fun generateUniqueTimestampId(): String {
        val dateFormat = SimpleDateFormat("yyMMddHHmmssSSS", Locale.getDefault())
        val timestampPart = dateFormat.format(Date())

        // Adiciona um sufixo aleatório para maior unicidade, ex: 4 caracteres alfanuméricos
        val randomSuffix = (1..4).map { Random.nextInt(0, 36).let {
            if (it < 10) '0' + it else ('A' + it - 10)
        }}.joinToString("")

        return "${timestampPart}_$randomSuffix"
    }
}