package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.ui.viewModels.ThemeMode
import com.example.myapplication.ui.viewModels.ThemeViewModel

@Composable
fun HomeScreen(navController: NavHostController, themeViewModel: ThemeViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                themeViewModel.setThemeMode(ThemeMode.SYSTEM)
            }, content = {
                Text("System Default")
            }, modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
        )
        Button(
            onClick = {
                themeViewModel.setThemeMode(ThemeMode.LIGHT)
            }, content = {
                Text("Light Theme")
            }, modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
        )
        Button(
            onClick = {
                themeViewModel.setThemeMode(ThemeMode.DARK)
            }, content = {
                Text("Dark Theme")
            }, modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
        )
    }
}