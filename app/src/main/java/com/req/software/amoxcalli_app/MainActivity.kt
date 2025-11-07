package com.req.software.amoxcalli_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.ui.screens.HomeScreen
import com.req.software.amoxcalli_app.ui.screens.LoginScreen
import com.req.software.amoxcalli_app.ui.theme.Amoxcalli_AppTheme
import com.req.software.amoxcalli_app.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Amoxcalli_AppTheme {
                AmoxcalliApp()
            }
        }
    }
}

@Composable
fun AmoxcalliApp() {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    if (currentUser != null) {
        HomeScreen(
            authViewModel = authViewModel
        )
    } else {
        LoginScreen(
            authViewModel = authViewModel
        )
    }
}