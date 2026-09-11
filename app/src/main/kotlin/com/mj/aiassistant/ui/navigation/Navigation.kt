package com.mj.aiassistant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mj.aiassistant.ui.screens.ChatScreen
import com.mj.aiassistant.ui.screens.HomeScreen
import com.mj.aiassistant.ui.screens.SettingsScreen
import com.mj.aiassistant.ui.screens.ToolsScreen

@Composable
fun MJNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "home"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("chat") {
            ChatScreen(navController = navController)
        }
        composable("tools") {
            ToolsScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}
