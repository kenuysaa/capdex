package com.example.capdex.ui.telas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.capdex.ui.embarcacao.CadastroEmbarcacaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriarEmbarcacao(
    navController: NavHostController,
    // ✅ 1. RECEBENDO O VIEWMODEL
    viewModel: CadastroEmbarcacaoViewModel = hiltViewModel()
) {
    // ... seus 'remember' para os campos ...
    var nomeEmbarcacao by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var nomeSetor by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmaSenha by remember { mutableStateOf("") }
    // ...

    // ✅ 2. OBSERVANDO O ESTADO DO VIEWMODEL
    val uiState by viewModel.uiState.collectAsState()

    // Lógica para navegar de volta quando o cadastro for bem-sucedido
    LaunchedEffect(uiState.sucesso) {
        if (uiState.sucesso) {
            navController.popBackStack() // Volta para a tela anterior
            viewModel.resetarEstado() // Limpa o estado para não navegar de novo
        }
    }

    // ... o resto do seu Scaffold e layout ...
    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(
        colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC))
    ))) {
        Scaffold(
            // ...
            // ✅ 3. ATUALIZANDO O BOTÃO "CRIAR"
            Button(
                onClick = {
                    viewModel.salvarEmbarcacao(
                        nomeEmbarcacao = nomeEmbarcacao,
                        cnpj = cnpj,
                        nomeSetorEncomenda = nomeSetor,
                        senhaSetorEncomenda = senha,
                        imageUri = imageUri
                    )
                },
                // ...
            ) {
                Text("Criar", /*...*/)
            }
            // ...
        )
    }
}

@Composable
fun LineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val corVerdePrincipal = Color(0xFF3E6340)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = corVerdePrincipal,
            focusedIndicatorColor = corVerdePrincipal,
            unfocusedIndicatorColor = corVerdePrincipal.copy(alpha = 0.5f),
            focusedTextColor = corVerdePrincipal,
            unfocusedTextColor = corVerdePrincipal,
            focusedLabelColor = corVerdePrincipal,
            unfocusedLabelColor = corVerdePrincipal.copy(alpha = 0.8f)
        )
    )
}