package com.example.capdex.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capdex.R

// Definição das cores
val verdeFundoEscuro = Color(0xFF2E7D32)
val verdeFormularioTranslucido = Color(0xB368A46A)
val corTextoBranco = Color.White
val corBordaCampo = Color.White
val corBotaoPrincipal = Color.White
val corTextoBotaoPrincipal = Color(0xFF1B5E20)

@Composable
fun CadastroScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegistrationSuccess: (Boolean) -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    val tipoContaSelecionado by authViewModel.tipoDeConta.collectAsState()

    var confirmaEmail by rememberSaveable { mutableStateOf("") }
    var CPF by rememberSaveable { mutableStateOf("") }
    var confirmaSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmaSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    var nomeError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var confirmaEmailError by remember { mutableStateOf<String?>(null) }
    var cpfError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }
    var confirmaSenhaError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = uiState.userUid) {
        if (uiState.userUid != null) {
            onRegistrationSuccess(uiState.isDono == true)
        }
    }

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
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(verdeFormularioTranslucido)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Criar Conta",
                    color = corTextoBranco,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                CadastroTextField(value = uiState.nomeCompleto, onValueChange = { authViewModel.onNomeCompletoChanged(it); nomeError = null }, label = "Nome Completo", isError = nomeError != null, supportingText = nomeError)
                Spacer(modifier = Modifier.height(12.dp))
                CadastroTextField(value = uiState.email, onValueChange = { authViewModel.onEmailChanged(it); emailError = null }, label = "E-mail", keyboardType = KeyboardType.Email, isError = emailError != null, supportingText = emailError)
                Spacer(modifier = Modifier.height(12.dp))
                CadastroTextField(value = confirmaEmail, onValueChange = { confirmaEmail = it; confirmaEmailError = null }, label = "Confirma e-mail", keyboardType = KeyboardType.Email, isError = confirmaEmailError != null, supportingText = confirmaEmailError)
                Spacer(modifier = Modifier.height(12.dp))
                CadastroTextField(value = CPF, onValueChange = { CPF = it }, label = "CPF", keyboardType = KeyboardType.Number, isError = cpfError != null, supportingText = cpfError)
                Spacer(modifier = Modifier.height(12.dp))
                CadastroTextField(value = uiState.password, onValueChange = { authViewModel.onPasswordChanged(it); senhaError = null }, label = "Senha", keyboardType = KeyboardType.Password, visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon = { val icon = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff; IconButton(onClick = { senhaVisivel = !senhaVisivel }) { Icon(icon, "Toggle senha", tint = corTextoBranco) } }, isError = senhaError != null, supportingText = senhaError)
                Spacer(modifier = Modifier.height(12.dp))
                CadastroTextField(value = confirmaSenha, onValueChange = { confirmaSenha = it; confirmaSenhaError = null }, label = "Confirma senha", keyboardType = KeyboardType.Password, visualTransformation = if (confirmaSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon = { val icon = if (confirmaSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff; IconButton(onClick = { confirmaSenhaVisivel = !confirmaSenhaVisivel }) { Icon(icon, "Toggle confirma senha", tint = corTextoBranco) } }, isError = confirmaSenhaError != null, supportingText = confirmaSenhaError)

                Spacer(modifier = Modifier.height(20.dp))

                Text("Tipo de conta", color = corTextoBranco, fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TipoContaRadioButton("Cliente", tipoContaSelecionado == "Cliente") {
                        authViewModel.onTipoDeContaChange("Cliente")
                    }
                    TipoContaRadioButton("Dono de Embarcação", tipoContaSelecionado == "Dono de Embarcação") {
                        authViewModel.onTipoDeContaChange("Dono de Embarcação")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(color = corTextoBranco)
                }
                uiState.errorMessage?.let { error ->
                    Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    // Lógica de validação...
                    authViewModel.registerUser(tipoContaSelecionado)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = corBotaoPrincipal, contentColor = corTextoBotaoPrincipal),
                enabled = !uiState.isLoading
            ) {
                Text(text = "Registrar", fontSize = 18.sp)
            }

            TextButton(onClick = onNavigateToLogin, enabled = !uiState.isLoading) {
                Text(text = "Tenho uma conta, Entrar", color = corTextoBranco, fontSize = 16.sp)
            }
        }
    }
}


// ===================================================================
// ✅ IMPLEMENTAÇÃO COMPLETA DAS FUNÇÕES AUXILIARES
// ===================================================================

@Composable
fun CadastroTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    label,
                    fontSize = 14.sp, // Ajuste de tamanho para melhor leitura
                    color = corTextoBranco.copy(alpha = 0.8f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, color = corTextoBranco), // Ajuste de tamanho
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = corTextoBranco,
                unfocusedTextColor = corTextoBranco,
                cursorColor = corTextoBranco,
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else corBordaCampo,
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.7f) else corBordaCampo.copy(alpha = 0.7f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(12.dp), // Aumentando o arredondamento
            isError = isError,
            supportingText = {
                if (supportingText != null) {
                    Text(
                        text = supportingText,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        )
    }
}

@Composable
fun TipoContaRadioButton(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50)) // Deixa mais arredondado
            .clickable(onClick = onSelect)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // O clique é no Row
            colors = RadioButtonDefaults.colors(
                selectedColor = corTextoBranco,
                unselectedColor = corTextoBranco.copy(alpha = 0.7f)
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = corTextoBranco, fontSize = 14.sp)
    }
}