package com.example.simplegallery.ui.screens.navigation

sealed class NavigationRoutes(val route: String) {
    object MainScreen : NavigationRoutes("main_screen")
    object ViewMediaScreen : NavigationRoutes("view_media_screen")
}