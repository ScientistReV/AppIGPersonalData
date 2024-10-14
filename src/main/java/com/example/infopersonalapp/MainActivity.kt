package com.example.infopersonalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.infopersonalapp.ui.theme.screens.AuthScreen
import com.example.infopersonalapp.ui.theme.screens.HomeScreen
import com.example.infopersonalapp.ui.theme.screens.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramComposeApp()
        }
    }
}

@Composable
fun InstagramComposeApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginClicked = {
                navController.navigate("auth")
            })
        }
        composable("auth") {
            AuthScreen(
                onAuthSuccess = { accessToken ->
                    // Salvar o accessToken conforme necessário
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(accessToken = "")
        }
    }
}
