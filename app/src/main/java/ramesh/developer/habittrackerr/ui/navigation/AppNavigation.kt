package ramesh.developer.habittrackerr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ramesh.developer.habittrackerr.ui.habitform.HabitFormScreen
import ramesh.developer.habittrackerr.ui.stats.StatsScreen
import ramesh.developer.habittrackerr.ui.today.TodayScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = TodayRoute
    ) {
        composable<TodayRoute> {
            TodayScreen(
                onNavigateToStats = { navController.navigate(StatsRoute) },
                onNavigateToCreate = { navController.navigate(HabitFormRoute()) },
                onNavigateToEdit = { id -> navController.navigate(HabitFormRoute(habitId = id)) }
            )
        }
        composable<StatsRoute> {
            StatsScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable<HabitFormRoute> {
            HabitFormScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}
