package com.example.bikestat.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavGraphSetup(navController: NavHostController, paddingsValues: PaddingValues){
    NavHost(navController = navController, startDestination = ScreenRoutes.HomeScreen.route, modifier = Modifier.padding(paddingsValues)){
        //Home Screen
        composable(
            route = ScreenRoutes.HomeScreen.route
        ){
            HomeScreenView(navController = navController)
        }
        //History Screen
        composable(
            route = ScreenRoutes.HistoryScreen.route
        ){
            HistoryScreenView(navController = navController)
        }
        //Statistics Screen
        composable(
            route = ScreenRoutes.StatisticsScreen.route
        ){
            StatisticsScreenView(navController = navController)
        }
    }
}