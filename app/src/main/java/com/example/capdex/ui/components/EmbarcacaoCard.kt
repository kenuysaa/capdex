package com.example.capdex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage

@Composable
fun EmbarcacaoCard(
    nome: String,
    rota: String,
    horario: String,
    imagemUrl: String, // << CORRIGIDO: era Int
    status: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 90.dp)
                .background(Color.Black, RoundedCornerShape(20.dp))
        ) {
            AsyncImage( // << CORRIGIDO: uso do Coil
                model = imagemUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            status?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(12.dp)
        ) {
            Text(nome, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(rota, fontSize = 12.sp, color = Color.Gray)
            Text("SEM PARADAS AO LONGO DA VIAGEM", fontSize = 10.sp, color = Color.Gray)
            Text(horario, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        }
    }
}
