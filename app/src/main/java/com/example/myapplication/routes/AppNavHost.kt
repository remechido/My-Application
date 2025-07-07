package com.example.myapplication.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.screens.AdminScreen
import com.example.myapplication.Pages.LoginPage
import com.example.myapplication.HomePage
import com.example.myapplication.MensagensScreen
import com.example.myapplication.PerfilScreen
import com.example.myapplication.navigation.Routes

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginPage { usuario, senha ->
                when {
                    usuario == "admin" && senha == "1234" -> {
                        navController.navigate("admin") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                    usuario.isNotBlank() && senha == "1234" -> {
                        navController.navigate(Routes.HOME_SCREEN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                    else -> {
                        // Pode mostrar erro, se quiser, usando estado em LoginPage
                    }
                }
            }
        }

        composable("admin") {
            AdminScreen(navController)
        }

        composable(Routes.HOME_SCREEN) {
            HomePage(navController)
        }


        composable("chat/{usuario}") { backStackEntry ->
            val usuario = backStackEntry.arguments?.getString("usuario") ?: "Desconhecido"
            MensagensScreen(username = usuario, navController = navController)
        }

        composable(Routes.PERFIL) {
            PerfilScreen(
                nome = "Laura Remechido",
                email = "26603@stu.ipbeja.pt",
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME_SCREEN) { inclusive = true }
                    }
                },
                onThemeChange = {},
                isDarkTheme = false
            )
        }
    }
}
