package com.example.capdex.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capdex.R

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToCadastro: () -> Unit,
    onLoginSuccess: (Boolean) -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = uiState.userUid) {
        if (uiState.userUid != null) {
            onLoginSuccess(uiState.isDono == true)
        }
    }

    // ✅ 1. ADICIONADO O BOX COM IMAGEM DE FUNDO E GRADIENTE
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(verdeFundoEscuro)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fundocadastro),
            contentDescription = "Fundo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ✅ 2. ADICIONADO O CONTAINER TRANSLÚCIDO PARA O FORMULÁRIO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(verdeFormularioTranslucido)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Login",
                    color = corTextoBranco,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo de E-mail
                CadastroTextField(
                    value = uiState.email,
                    onValueChange = { authViewModel.onEmailChanged(it) },
                    label = "E-mail"
                )
                Spacer(modifier = Modifier.height(16.dp))

                // ✅ 3. CAMPO DE SENHA AGORA TEM O ÍCONE DE VISIBILIDADE
                CadastroTextField(
                    value = uiState.password,
                    onValueChange = { authViewModel.onPasswordChanged(it) },
                    label = "Senha",
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(icon, contentDescription = "Toggle senha", tint = corTextoBranco)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(color = corTextoBranco)
                }

                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Botão Entrar
            Button(
                onClick = { authViewModel.loginUser() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = corBotaoPrincipal,
                    contentColor = corTextoBotaoPrincipal
                ),
                enabled = !uiState.isLoading
            ) {
                Text(text = "Entrar", fontSize = 18.sp)
            }

            // Botão Cadastrar
            TextButton(onClick = onNavigateToCadastro, enabled = !uiState.isLoading) {
                Text(text = "Não tenho uma conta, Cadastrar", color = corTextoBranco, fontSize = 16.sp)
            }
        }
    }
}