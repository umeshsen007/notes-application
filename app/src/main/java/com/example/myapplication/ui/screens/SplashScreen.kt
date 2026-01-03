package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navigation.NavDest
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(2000)
        
        // Check if user is already logged in
        val isLoggedIn = authViewModel.isUserLoggedIn()
        val destination = if (isLoggedIn) {
            NavDest.NOTES_LIST
        } else {
            NavDest.LOGIN
        }
        
        navController.navigate(destination) {
            popUpTo(NavDest.SPLASH_SCREEN) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "My Notes",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    MyApplicationTheme {
        SplashScreen(rememberNavController())
    }
}