package com.frcoding.reatailcashregister.screens

const val TOTAL_PRICE = "total_price"
sealed class Screen(val route: String) {
    // add pass methods if needed
    object SignIn: Screen("sign_in_screen")
    object SignUp: Screen("sign_up_screen")
    object Main: Screen("main_screen")
    object InvoicesReview: Screen("invoices_review_screen")
    object Payment: Screen("payment_screen/{$TOTAL_PRICE}") {
        fun passTotalPrice(totalPrice: String): String {
            return "payment_screen/$totalPrice"
        }
    }
}