package com.frcoding.reatailcashregister

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.screens.Screen
import com.frcoding.reatailcashregister.screens.TOTAL_PRICE
import com.frcoding.reatailcashregister.screens.invoices_review.InvoicesReviewScreen
import com.frcoding.reatailcashregister.screens.main.MainScreen
import com.frcoding.reatailcashregister.screens.payment.PaymentScreen
import com.frcoding.reatailcashregister.screens.sign_in.SignInScreen
import com.frcoding.reatailcashregister.screens.sign_up.SignUpScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    sessionManager: SessionManager
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ){
        composable(
            route = Screen.SignIn.route
        ){
            SignInScreen(navController = navController)
        }

        composable(
            route = Screen.SignUp.route
        ){
            SignUpScreen(navController = navController)
        }

        composable(
            route = Screen.Main.route
        ) {
            MainScreen(navController = navController, sessionManager = sessionManager)
        }

        composable(
            route = Screen.InvoicesReview.route
        ) {
            InvoicesReviewScreen()
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                navArgument(TOTAL_PRICE) {
                    type = NavType.StringType
                }
            )
        ) {
            PaymentScreen(navController = navController, sessionManager = sessionManager)
        }
    }
}