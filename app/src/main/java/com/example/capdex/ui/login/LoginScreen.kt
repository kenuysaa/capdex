package com.example.capdex.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capdex.R
import com.example.capdex.presentation.AuthViewModel
import com.example.capdex.ui.cadastro.*

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToCadastro: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()

    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var hasNavigatedOnSuccess by remember { mutableStateOf(false) }

    // Navegação ao sucesso
    LaunchedEffect(key1 = uiState.userUid, key2 = uiState.successMessage) {
        if (uiState.userUid != null && uiState.successMessage != null && !hasNavigatedOnSuccess) {
            onLoginSuccess(uiState.userUid!!)
            hasNavigatedOnSuccess = true
        }
    }

    // Fundo da tela
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(verdeFundoEscuro)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fundologin),
            contentDescription = "Fundo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Cartão do formulário
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(verdeFormularioTranslucido)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    color = corTextoBranco,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo de e-mail
                CadastroTextField(
                    value = uiState.email,
                    onValueChange = { authViewModel.onEmailChanged(it) },
                    label = "E-mail",
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo de senha
                CadastroTextField(
                    value = uiState.password,
                    onValueChange = { authViewModel.onPasswordChanged(it) },
                    label = "Senha",
                    keyboardType = KeyboardType.Password,
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
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Erro geral
                uiState.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }


            }
            Spacer(modifier = Modifier.height(25.dp))
            // Botão de login
            Button(
                onClick = { authViewModel.loginUser() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = corBotaoPrincipal,
                    contentColor = corTextoBotaoPrincipal
                )
            ) {
                Text("Entrar", fontSize = 18.sp)
            }

            // Link para cadastro
            TextButton(onClick = onNavigateToCadastro, enabled = !uiState.isLoading) {
                Text(
                    text = "Não tem uma conta? Cadastre-se",
                    color = corTextoBranco,
                    fontSize = 16.sp
                )
            }
        }
    }
}
