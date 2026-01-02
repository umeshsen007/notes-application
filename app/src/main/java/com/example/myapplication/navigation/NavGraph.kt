package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.SplashScreen
import com.example.myapplication.ui.viewModels.ThemeViewModel

@Composable
fun NavComposable(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDest.SPLASH_SCREEN,

) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        composable(NavDest.SPLASH_SCREEN) {
            SplashScreen(navController)
        }

        composable(NavDest.HOME) {
            HomeScreen(navController)
        }
    }
}