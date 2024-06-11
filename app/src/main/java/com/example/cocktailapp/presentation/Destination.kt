package com.example.cocktailapp.presentation

sealed class Destination(val route: String) {
    data object HomeScreen: Destination("home_screen")
    data class DetailScreen(val cocktailId: String): Destination("detail_screen/$cocktailId") {
        companion object {
            const val ROUTE_DEFINITION = "detail_screen/{cocktailId}"
        }
    }
}