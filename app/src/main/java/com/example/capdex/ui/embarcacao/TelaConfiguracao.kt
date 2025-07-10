package com.example.capdex.ui.embarcacao


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capdex.R // Importe seu R
import com.example.capdex.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConfiguracao(navController: NavHostController) {

    val selectedIndex = remember { mutableStateOf(2) } // 2 para "Configurações"

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3E6340),
            Color(0xFF9BFBE8),
            Color(0xFFC9FFF4),
            Color(0xFFC9FFF4)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Minha conta",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(50)),
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                // Navegação para Tela Principal
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, contentDescription = "Embarcações") },
                    selected = selectedIndex.value == 0,
                    onClick = {
                        selectedIndex.value = 0
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    },
                    alwaysShowLabel = false
                )
                // Navegação para Pacotes
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Pacotes") },
                    selected = selectedIndex.value == 1,
                    onClick = {
                        selectedIndex.value = 1
                        navController.navigate(Screen.Carga.route)
                    },
                    alwaysShowLabel = false
                )
                // Item atual
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Configurações") },
                    selected = selectedIndex.value == 2,
                    onClick = { /* Já está nesta tela */ },
                    alwaysShowLabel = false
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Seção do Perfil
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: Substitua 'profile_placeholder' pelo nome da sua imagem de capivara
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "Foto de Perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Karlos Kook", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("karloskook@gmail.com", color = Color.White.copy(alpha = 0.8f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Ação para editar perfil */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Perfil", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Seção de Opções
            Text("Opções", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Opção Suporte
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Ação para Suporte */ }
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = "Suporte", tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Suporte", color = Color.White, fontSize = 18.sp)
            }

            // Opção Sair
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Logout.route) } // Navega para Logout
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair", tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Sair", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}