package ng.chimdike.githubproject.home.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.chimdike.core.navigation.NavigationScreens
import ng.chimdike.githubproject.home.ui.home.compose.DetailsScreen
import ng.chimdike.githubproject.home.ui.home.compose.HomeScreen
import ng.chimdike.githubproject.home.ui.home.viewmodel.HomeViewModel

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    viewModel: HomeViewModel
){
    navigation(startDestination = NavigationScreens.HomeRoute.route, route = NavigationScreens.HomeScreen.route){
        composable(route = NavigationScreens.HomeRoute.route){
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = NavigationScreens.DetailsScreen.route){
            DetailsScreen(
                viewModel = viewModel,
                navController = navController,
                )
        }
    }
}
