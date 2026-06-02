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
import com.example.capdex.R
import com.example.capdex.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConfiguracao(
    navController: NavHostController,
    // ✅ Assinatura da função corrigida para aceitar os parâmetros
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit
) {
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
                title = { Text("Minha conta", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
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
                containerColor = Color.White
            ) {
                // ✅ Barra de navegação usando o estado vindo do AppNavGraph
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, "Embarcações") },
                    selected = selectedIndex == 0,
                    onClick = {
                        onSelectedIndexChange(0)
                        navController.popBackStack()
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, "Configurações") },
                    selected = selectedIndex == 2,
                    onClick = { onSelectedIndexChange(2) }
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Foto de Perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Karlos Kook", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("karloskook@gmail.com", color = Color.White.copy(alpha = 0.8f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Ação para editar perfil */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Perfil", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text("Opções", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { /* Ação para Suporte */ }.padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = "Suporte", tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Suporte", color = Color.White, fontSize = 18.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Screen.Logout.route) }.padding(vertical = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair", tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Sair", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}