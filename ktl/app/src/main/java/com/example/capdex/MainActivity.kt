package com.example.capdex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.capdex.ui.theme.CapDexTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.capdex.ui.navigation.AppNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CapDexTheme (dynamicColor = false) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }

    }
}