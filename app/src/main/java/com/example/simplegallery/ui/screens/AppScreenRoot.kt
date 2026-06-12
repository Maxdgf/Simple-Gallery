package com.example.simplegallery.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplegallery.ui.screens.navigation.NavigationRoutes
import com.example.simplegallery.ui.screens.navigation.Navigator
import com.example.simplegallery.ui.viewmodels.GalleryState

@Composable
fun AppScreenRoot() {
    val galleryState: GalleryState = hiltViewModel()

    val navController = rememberNavController()
    val navigator = remember { Navigator(navController) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route
        ) {
            // === Main Screen ===
            composable(route = NavigationRoutes.MainScreen.route) {
                MainAppScreen(
                    galleryState = galleryState,
                    onNavigateTo = navigator::navigateTo
                )
            }

            // === View Media Screen ===
            composable(route = NavigationRoutes.ViewMediaScreen.route) {
                ViewMediaAppScreen(
                    galleryState = galleryState,
                    onNavigateTo = navigator::navigateTo
                )
            }
        }
    }
}