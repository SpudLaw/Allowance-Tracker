package com.cal.allowancetracker.navigation

import HomeScreen
import PurchaseEditScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cal.allowancetracker.AppViewModelProvider
import com.cal.allowancetracker.purchase.PurchaseEditViewModel
import com.cal.allowancetracker.purchase.PurchaseEntryDestination
import com.cal.allowancetracker.purchase.PurchaseEntryScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AllowanceNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(PurchaseEntryDestination.route) },
                navigateToItemUpdate = {
                    Log.d("HomeScreen", "Navigating to PurchaseEdit with ID: $it")
                    navController.navigate("${PurchaseEditDestination.route}/${it}")
                }
            )
        }
        composable(route = PurchaseEntryDestination.route) {
            PurchaseEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = PurchaseEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PurchaseEditDestination.purchaseIdArg) {
                type = NavType.IntType
            })
        ) {
            val purchaseId = it.arguments?.getInt(PurchaseEditDestination.purchaseIdArg)
            Log.d("PurchaseEditScreen", "Received Purchase ID: $purchaseId")
            val viewModel: PurchaseEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
            if (purchaseId != null) {
                viewModel.purchaseId = purchaseId
            }
            PurchaseEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}
