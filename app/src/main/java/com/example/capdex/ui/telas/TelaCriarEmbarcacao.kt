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
    viewModel: CadastroEmbarcacaoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmaSenha by rememberSaveable { mutableStateOf("") }
    var confirmaSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> viewModel.onImageUriChange(uri) }
    )

    LaunchedEffect(uiState.cadastroSucesso) {
        if (uiState.cadastroSucesso) {
            navController.popBackStack()
            viewModel.resetarEstado()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(
        colors = listOf(Color(0xFF3E6340), Color(0xFF8DE9C3), Color(0xFFB3F5DC))
    ))) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Criar Embarcações", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.imageUri != null) {
                        AsyncImage(
                            model = uiState.imageUri,
                            contentDescription = "Imagem da Embarcação",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = "Selecionar Imagem",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                LineTextField(value = uiState.nomeEmbarcacao, onValueChange = viewModel::onNomeChange, label = "Nome da embarcação")
                Spacer(modifier = Modifier.height(16.dp))
                LineTextField(value = uiState.cnpj, onValueChange = viewModel::onCnpjChange, label = "CNPJ")
                Spacer(modifier = Modifier.height(16.dp))
                LineTextField(value = uiState.nomeSetor, onValueChange = viewModel::onNomeSetorChange, label = "Nome do setor de encomenda")
                Spacer(modifier = Modifier.height(16.dp))
                LineTextField(
                    value = uiState.senhaSetor,
                    onValueChange = viewModel::onSenhaChange,
                    label = "Senha",
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password,
                    trailingIcon = {
                        val icon = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(icon, contentDescription = "Toggle senha", tint = Color(0xFF3E6340))
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                LineTextField(
                    value = confirmaSenha,
                    onValueChange = { confirmaSenha = it },
                    label = "Confirme sua senha",
                    visualTransformation = if (confirmaSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password,
                    trailingIcon = {
                        val icon = if (confirmaSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmaSenhaVisivel = !confirmaSenhaVisivel }) {
                            Icon(icon, contentDescription = "Toggle confirma senha", tint = Color(0xFF3E6340))
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.salvarEmbarcacao() },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF3E6340))
                    } else {
                        Text("Criar", color = Color(0xFF3E6340), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
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