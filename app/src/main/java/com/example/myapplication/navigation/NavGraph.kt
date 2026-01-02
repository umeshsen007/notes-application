package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.SplashScreen
import com.example.myapplication.ui.screens.auth.LoginScreen
import com.example.myapplication.ui.screens.auth.SignupScreen
import com.example.myapplication.ui.screens.notes.AddEditNoteScreen
import com.example.myapplication.ui.screens.notes.NotesListScreen

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

        composable(NavDest.LOGIN) {
            LoginScreen(navController)
        }

        composable(NavDest.SIGNUP) {
            SignupScreen(navController)
        }

        composable(NavDest.NOTES_LIST) {
            NotesListScreen(navController)
        }

        composable(
            route = "${NavDest.ADD_EDIT_NOTE}/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    defaultValue = "-1"
                }
            )
        ) {
            AddEditNoteScreen(navController)
        }
    }
}