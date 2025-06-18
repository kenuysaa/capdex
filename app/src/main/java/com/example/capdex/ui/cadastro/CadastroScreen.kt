package com.example.capdex.ui.cadastro

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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capdex.R
import com.example.capdex.presentation.AuthViewModel
import com.example.capdex.ui.navigation.Screen

// =====================
// Definição das cores usadas no formulário
// =====================
val verdeFundoEscuro = Color(0xFF2E7D32)
val verdeFormularioTranslucido = Color(0xB368A46A)
val corTextoBranco = Color.White
val corBordaCampo = Color.White
val corBotaoPrincipal = Color.White
val corTextoBotaoPrincipal = Color(0xFF1B5E20)

// =====================
// Composable principal da tela de cadastro
// =====================
@Composable
fun CadastroScreen(
    authViewModel: AuthViewModel,       // ViewModel para manipular estado e lógica
    onNavigateToLogin: () -> Unit,      // Callback para navegar para tela de login
    onRegistrationSuccess: () -> Unit   // Callback para ação após cadastro bem-sucedido
) {
    // Estado da UI vindo do ViewModel (nome, email, senha, loading, erros etc)
    val uiState by authViewModel.uiState.collectAsState()

    // Estados locais para confirmação de email e senha e visibilidade dos campos de senha
    var confirmaEmail by rememberSaveable { mutableStateOf("") }
    var confirmaSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmaSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    // Estado para controle do tipo de conta (radio button)
    var tipoContaUILabel by rememberSaveable { mutableStateOf("Cliente") }

    // Variáveis para erros de validação local da tela
    var nomeError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var confirmaEmailError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }
    var confirmaSenhaError by remember { mutableStateOf<String?>(null) }

    // Controle para evitar múltiplas navegações após sucesso
    var hasNavigatedOnSuccess by rememberSaveable { mutableStateOf(false) }

    // =====================
    // Efeito colateral: Navega para tela de sucesso após cadastro
    // =====================
    LaunchedEffect(key1 = uiState.userUid, key2 = uiState.successMessage) {
        if (uiState.userUid != null && uiState.successMessage != null && !hasNavigatedOnSuccess) {
            onRegistrationSuccess()
            hasNavigatedOnSuccess = true
        }
    }

    // =====================
    // Layout geral da tela - fundo e estrutura de scroll
    // =====================
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(verdeFundoEscuro)  // Cor de fundo da tela
    ) {
        // Imagem de fundo em toda a tela
        Image(
            painter = painterResource(id = R.drawable.fundocadastro),
            contentDescription = "Fundo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomCenter)
        )

        // Coluna principal com scroll vertical e padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Espaço superior

            // Card / container do formulário com cantos arredondados e fundo translúcido
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(verdeFormularioTranslucido)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Criar Conta",
                    color = corTextoBranco,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo Nome Completo
                CadastroTextField(
                    value = uiState.nomeCompleto,
                    onValueChange = {
                        authViewModel.onNomeCompletoChanged(it)
                        nomeError = null
                    },
                    label = "Nome Completo",
                    isError = nomeError != null,
                    supportingText = nomeError
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo E-mail
                CadastroTextField(
                    value = uiState.email,
                    onValueChange = {
                        authViewModel.onEmailChanged(it)
                        emailError = null
                    },
                    label = "E-mail",
                    keyboardType = KeyboardType.Email,
                    isError = emailError != null,
                    supportingText = emailError
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo Confirma E-mail (local)
                CadastroTextField(
                    value = confirmaEmail,
                    onValueChange = {
                        confirmaEmail = it
                        confirmaEmailError = null
                    },
                    label = "Confirma e-mail",
                    keyboardType = KeyboardType.Email,
                    isError = confirmaEmailError != null,
                    supportingText = confirmaEmailError
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo Senha
                CadastroTextField(
                    value = uiState.password,
                    onValueChange = {
                        authViewModel.onPasswordChanged(it)
                        senhaError = null
                    },
                    label = "Senha",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(icon, contentDescription = "Toggle senha", tint = corTextoBranco)
                        }
                    },
                    isError = senhaError != null,
                    supportingText = senhaError
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo Confirma Senha (local)
                CadastroTextField(
                    value = confirmaSenha,
                    onValueChange = {
                        confirmaSenha = it
                        confirmaSenhaError = null
                    },
                    label = "Confirma senha",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (confirmaSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (confirmaSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmaSenhaVisivel = !confirmaSenhaVisivel }) {
                            Icon(icon, contentDescription = "Toggle confirma senha", tint = corTextoBranco)
                        }
                    },
                    isError = confirmaSenhaError != null,
                    supportingText = confirmaSenhaError
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Label do tipo de conta
                Text(
                    "Tipo de conta",
                    color = corTextoBranco,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Opções tipo de conta com RadioButtons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TipoContaRadioButton("Cliente", tipoContaUILabel == "Cliente") {
                        tipoContaUILabel = "Cliente"
                    }
                    TipoContaRadioButton("Dono de Embarcação", tipoContaUILabel == "Dono de Embarcação") {
                        tipoContaUILabel = "Dono de Embarcação"
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Loader de progresso enquanto carrega
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = corTextoBranco)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Mensagem de erro geral vinda do ViewModel
                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Botão Registrar - com validação antes de chamar o ViewModel
            Button(
                onClick = {
                    // Resetar erros locais
                    nomeError = null; emailError = null; confirmaEmailError = null; senhaError = null; confirmaSenhaError = null
                    var isValid = true

                    // Validação dos campos
                    if (uiState.nomeCompleto.isBlank()) {
                        nomeError = "Nome não pode estar vazio"
                        isValid = false
                    } else if (uiState.email.isBlank()) {
                        emailError = "E-mail não pode estar vazio"
                        isValid = false
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) {
                        emailError = "Formato de e-mail inválido"
                        isValid = false
                    }
                    if (confirmaEmail != uiState.email) {
                        confirmaEmailError = "Os e-mails não coincidem"
                        isValid = false
                    }
                    if (uiState.password.isBlank()) {
                        senhaError = "Senha não pode estar vazia"
                        isValid = false
                    } else if (uiState.password.length < 6) {
                        senhaError = "Senha deve ter pelo menos 6 caracteres"
                        isValid = false
                    }
                    if (confirmaSenha != uiState.password) {
                        confirmaSenhaError = "As senhas não coincidem"
                        isValid = false
                    }

                    if (isValid) {
                        hasNavigatedOnSuccess = false // Permitir nova navegação no sucesso
                        val userTypeBackend = when (tipoContaUILabel) {
                            "Cliente" -> "comum"
                            "Dono de Embarcação" -> "proprietario"
                            else -> "comum"
                        }
                        authViewModel.registerUser(userTypeBackend)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = corBotaoPrincipal,
                    contentColor = corTextoBotaoPrincipal
                ),
                enabled = !uiState.isLoading
            ) {
                Text(text = "Registrar", fontSize = 18.sp)
            }

            // Botão para navegar para login
            TextButton(onClick = onNavigateToLogin, enabled = !uiState.isLoading) {
                Text(
                    text = "Tenho uma conta, Entrar",
                    color = corTextoBranco,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// =====================
// Composable para campos de texto do formulário
// =====================
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
                    fontSize = 11.sp,
                    color = corTextoBranco.copy(alpha = 0.8f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(fontSize = 13.sp, color = corTextoBranco),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = corTextoBranco,
                unfocusedTextColor = corTextoBranco,
                cursorColor = corTextoBranco,
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else corBordaCampo,
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.7f) else corBordaCampo.copy(alpha = 0.7f),
                focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else corTextoBranco,
                unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.8f) else corTextoBranco.copy(alpha = 0.8f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorTextColor = corTextoBranco,
                errorSupportingTextColor = MaterialTheme.colorScheme.error
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(6.dp),
            isError = isError
        )
        if (supportingText != null) {
            Text(
                text = supportingText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}

// =====================
// Composable para o radio button do tipo de conta
// =====================
@Composable
fun TipoContaRadioButton(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onSelect)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = corTextoBranco,
                unselectedColor = corTextoBranco.copy(alpha = 0.7f)
            ),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = corTextoBranco, fontSize = 12.sp)
    }
}
