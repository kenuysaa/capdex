package com.example.capdex.ui.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capdex.data.model.Usuario
import com.example.capdex.ui.theme.CapDexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onRegistrationSuccess: (String) -> Unit, // Callback with UID on successful registration
    onNavigateToLogin: () -> Unit // Callback to navigate to login screen
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var userCpf by remember { mutableStateOf("") }
    var isProprietario by remember { mutableStateOf(false) }

    val registrationResult by authViewModel.registrationResult.collectAsState()
    val addUserDataResult by userViewModel.addUpdateDeleteResult.collectAsState()

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle registration result
    LaunchedEffect(registrationResult) {
        registrationResult?.onSuccess { uid ->
            if (uid != null) {
                snackbarMessage = "Registro bem-sucedido! Adicionando dados do usuário..."
                // Immediately add user data to Firestore after successful Firebase Auth registration
                val newUser = Usuario(
                    idUser = uid,
                    nome = userName.ifEmpty { "Usuário Cadastrado" },
                    email = email,
                    cpf = userCpf.ifEmpty { "Não Informado" },
                    proprietario = isProprietario
                )
                userViewModel.addUsuario(newUser)
            } else {
                snackbarMessage = "Falha no registro: UID nulo. Tente novamente."
            }
            authViewModel.resetRegistrationResult()
        }?.onFailure { e ->
            snackbarMessage = "Erro no registro: ${e.message ?: "Erro desconhecido"}"
            authViewModel.resetRegistrationResult()
        }
    }

    // Handle user data addition result (after registration)
    LaunchedEffect(addUserDataResult) {
        addUserDataResult?.onSuccess {
            snackbarMessage = "Dados do usuário adicionados com sucesso!"
            // Clear fields on success
            email = ""
            password = ""
            userName = ""
            userCpf = ""
            isProprietario = false
            // Navigate to appropriate screen (e.g., login or home if auto-logged in)
            authViewModel.currentUserUid.value?.let { uid ->
                onRegistrationSuccess(uid) // Notify parent that registration and data addition is complete
            }
        }?.onFailure { e ->
            snackbarMessage = "Erro ao adicionar dados do usuário: ${e.message ?: "Erro desconhecido"}"
        }
        userViewModel.resetAddUpdateDeleteResult()
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            snackbarMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Criar Nova Conta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userCpf,
                onValueChange = { userCpf = it },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Sou Proprietário:")
                Checkbox(
                    checked = isProprietario,
                    onCheckedChange = { isProprietario = it }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    authViewModel.registerUser(email, password)
                },
                enabled = email.isNotBlank() && password.isNotBlank() &&
                        userName.isNotBlank() && userCpf.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Já tem uma conta? Faça Login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    CapDexTheme {
        RegisterScreen(
            onRegistrationSuccess = {},
            onNavigateToLogin = {}
        )
    }
}