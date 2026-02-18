package com.daclan.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daclan.mobile.ui.theme.MobileTheme
import com.daclan.mobile.util.Prefs
import com.daclan.mobile.screens.LoginScreen
import com.daclan.mobile.screens.RegisterScreen
import com.daclan.mobile.screens.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileTheme {
                val navController = rememberNavController()
                val startDest by remember {
                    mutableStateOf(
                        if (Prefs.getToken(this) == null) "login" else "profile"
                    )
                }
                AppNavHost(navController, startDest)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, startDest: String) {
    NavHost(navController = navController, startDestination = startDest) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("profile") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegistered = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            DashboardScreen(
                onLoggedOut = {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileTheme {
        LoginScreen(onLoginSuccess = {}, onGoRegister = {})
    }
}