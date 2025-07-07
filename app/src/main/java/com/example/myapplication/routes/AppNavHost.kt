package com.example.myapplication.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Pages.AdminScreen
import com.example.myapplication.Pages.LoginPage
import com.example.myapplication.HomePage
import com.example.myapplication.MensagensScreen
import com.example.myapplication.PerfilScreen
import com.example.myapplication.navigation.Routes
import com.example.myapplication.viewmodel.AuthViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginPage(
                onLoginSuccess = { isAdmin ->
                    if (isAdmin) {
                        navController.navigate("admin") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.HOME_SCREEN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable("admin") {
            AdminScreen(
                navController = navController,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo("admin") { inclusive = true }
                    }
                }
            )
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
                    authViewModel.logout()
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
