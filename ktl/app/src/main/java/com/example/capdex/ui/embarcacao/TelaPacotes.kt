package com.example.capdex.ui.embarcacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.capdex.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPacotes(
    navController: NavHostController,
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
        topBar = {},
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(50)),
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Sailing, contentDescription = "Embarcações") },
                    selected = selectedIndex == 0,
                    onClick = {
                        onSelectedIndexChange(0)
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Pacotes") },
                    selected = selectedIndex == 1,
                    onClick = { onSelectedIndexChange(1) } // Já está nesta tela
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = "Configurações") },
                    selected = selectedIndex == 2,
                    onClick = {
                        onSelectedIndexChange(2)
                        navController.navigate(Screen.Config.route)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tela de Pacotes",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
