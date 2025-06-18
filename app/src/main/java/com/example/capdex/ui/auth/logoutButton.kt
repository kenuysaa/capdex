package com.example.capdex.ui.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capdex.ui.theme.CapDexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutButton(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit // Callback to navigate after logout
) {
    val signOutSuccess by authViewModel.signOutSuccess.collectAsState()
    val currentUserUid by authViewModel.currentUserUid.collectAsState()

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(signOutSuccess) {
        if (signOutSuccess) {
            snackbarMessage = "Logout bem-sucedido!"
            onLogoutSuccess() // Trigger navigation
            authViewModel.resetSignOutSuccess() // Consume the event
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            snackbarMessage = null
        }
    }

    // Scaffold for the snackbar, if this button is used as a standalone composable.
    // In a real app, the parent screen might provide the Scaffold.
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Button(
            onClick = { authViewModel.signOut() },
            enabled = currentUserUid != null, // Enable only if a user is logged in
            modifier = modifier.padding(paddingValues) // Apply padding from Scaffold if used
        ) {
            Text("Sair")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoutButton() {
    CapDexTheme {
        LogoutButton(onLogoutSuccess = {})
    }
}