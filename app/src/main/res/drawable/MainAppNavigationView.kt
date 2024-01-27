package com.example.bikestattest

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bikestat.Navigation.NavGraphSetup
import com.example.bikestat.Navigation.ScreenRoutes
import com.example.bikestattest.ui.theme.MainOrange

@Composable
fun MainAppNavigationView(){
    val navHostController = rememberNavController()
    val itemsInNavBar = listOf(
        BottomNavItem(
            title = "История",
            icon = R.drawable.history_icon,
            screenRoute = ScreenRoutes.HistoryScreen.route,
        ),
        BottomNavItem(
            title = "Маршрут",
            icon = R.drawable.route_icon,
            screenRoute = ScreenRoutes.HomeScreen.route,
        ),
        BottomNavItem(
            title = "Статистика",
            icon = R.drawable.graph_icon,
            screenRoute = ScreenRoutes.StatisticsScreen.route,
        ),
    )
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, contentColor = Color.Black) {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                itemsInNavBar.forEach { bottomNavItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any{it.route == bottomNavItem.screenRoute} == true,
                        onClick = {
                            navHostController.navigate(bottomNavItem.screenRoute){
                                popUpTo(navHostController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(bottomNavItem.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = bottomNavItem.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = MainOrange,
                            selectedTextColor = MainOrange,
                            indicatorColor = MainOrange,
                            unselectedTextColor = MainOrange,
                        ))
                }
            }
        }
    ) { paddingValues ->
        NavGraphSetup(navController = navHostController, paddingsValues = paddingValues)
    }
}
data class BottomNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String,
)