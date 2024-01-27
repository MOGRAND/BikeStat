package com.example.bikestat.Navigation

sealed class ScreenRoutes(val route : String) {

    object HomeScreen : ScreenRoutes(route = "home_screen")

    object HistoryScreen: ScreenRoutes(route = "history_screen")

    object StatisticsScreen: ScreenRoutes(route = "statistics_screen")


}