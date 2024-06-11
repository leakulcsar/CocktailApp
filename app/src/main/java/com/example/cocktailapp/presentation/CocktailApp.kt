package com.example.cocktailapp.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cocktailapp.presentation.detail.DetailScreen
import com.example.cocktailapp.presentation.home.HomeScreen

@Composable
fun CocktailApp(
) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = Destination.HomeScreen.route
    ) {
        composable(Destination.HomeScreen.route) {
            HomeScreen(
                homeViewModel = hiltViewModel(),
                onNavigateToCocktailDetail = { cocktailId ->

                    navController.navigate(Destination.DetailScreen(cocktailId = cocktailId).route)
                }
            )
        }
        composable(
            route = Destination.DetailScreen.ROUTE_DEFINITION,
            arguments = listOf(navArgument("cocktailId") { type = NavType.StringType })
        ) {
            DetailScreen(
                detailViewModel = hiltViewModel(viewModelStoreOwner = it),
                onNavigateBack = { navController.navigateUp() })
        }
    }
}