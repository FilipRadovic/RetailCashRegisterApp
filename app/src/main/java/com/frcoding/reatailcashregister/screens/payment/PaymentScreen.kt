package com.frcoding.reatailcashregister.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.frcoding.reatailcashregister.R
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.screens.InvoiceViewModel
import com.frcoding.reatailcashregister.screens.main.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    invoiceViewModel: InvoiceViewModel = hiltViewModel(),
    sessionManager: SessionManager
) {
    val userId by remember {
        mutableStateOf(sessionManager.getUserId() ?: -1)
    }

    val totalPriceString = navController.currentBackStackEntry?.arguments?.getString("total_price")
    val totalPrice = totalPriceString?.toDoubleOrNull() ?: 0.0

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var isCreditCardSheetVisible by remember { mutableStateOf(false) }
    var isCashSheetVisible by remember { mutableStateOf(false) }

    fun openBottomSheet() {
        coroutineScope.launch {
            isCreditCardSheetVisible = true
            sheetState.show()
        }
    }

    fun openCashSheet() {
        coroutineScope.launch {
            isCashSheetVisible = true
            sheetState.show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // First Row with text
        Text(
            text = "Select a payment method:",
            style = MaterialTheme.typography.headlineLarge,  // Larger text style
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Second Row with payment method icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Column for Credit Card
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.credit_card),  // Icon for credit card
                    contentDescription = "Credit Card",
                    modifier = Modifier
                        .size(120.dp)  // Adjust size as needed
                        .clickable {
                            openBottomSheet()
                        }
                )
                PaymentBottomSheet(
                    isBottomSheetVisible = isCreditCardSheetVisible,
                    sheetState = sheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            isCreditCardSheetVisible = false
                            sheetState.hide()
                        }
                    },
                    totalPrice = totalPrice,
                    invoiceViewModel = invoiceViewModel,
                    navController = navController,
                    userId = userId
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Credit Card",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
            }

            // Column for Cash
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cash),  // Icon for cash
                    contentDescription = "Cash",
                    modifier = Modifier
                        .size(120.dp)  // Adjust size as needed
                        .clickable {
                            openCashSheet()
                        }
                )
                CashBottomSheet(
                    isBottomSheetVisible = isCashSheetVisible,
                    sheetState = sheetState,
                    onDismiss = {
                        coroutineScope.launch {
                            isCashSheetVisible = false
                            sheetState.hide()
                        }
                    },
                    totalPrice = totalPrice,
                    invoiceViewModel = invoiceViewModel,
                    navController = navController,
                    userId = userId
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Cash",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp
                )
            }
        }
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    totalPrice: Double,
    mainViewModel: MainViewModel = hiltViewModel(),
    invoiceViewModel: InvoiceViewModel,
    navController: NavController,
    userId: Int
) {
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RectangleShape,
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = .5f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)  // Adjust padding as needed
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(16.dp),  // Inner padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Dismiss the dialog"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Text "Total to pay:"
                Text(
                    text = "Total to pay:",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Total Price Field
                Text(
                    text = "%.2f".format(totalPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Finish Button
                Button(
                    onClick = {
                        // Handle Finish action
                        invoiceViewModel.addInvoice(
                            userId = userId,
                            totalPrice = totalPrice,
                            "Credit Card"
                        )
                        mainViewModel.deleteAllItems()
                        onDismiss()
                        navController.navigate("main_screen") {
                            popUpTo("payment_screen") {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Finish")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashBottomSheet(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    totalPrice: Double,
    mainViewModel: MainViewModel = hiltViewModel(),
    invoiceViewModel: InvoiceViewModel,
    navController: NavController,
    userId: Int
) {
    // State variables for user input and validation
    var amountPaid by remember { mutableStateOf("") }
    var changeToReturn by remember { mutableStateOf("0.0") }
    var isAmountValid by remember { mutableStateOf(true) }

    // Recalculate change when amountPaid or totalPrice changes
    LaunchedEffect(amountPaid, totalPrice) {
        val amount = amountPaid.toDoubleOrNull() ?: 0.0
        isAmountValid = amount >= totalPrice
        changeToReturn = if (isAmountValid) "%.2f".format(amount - totalPrice) else "0.0"
    }

    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RectangleShape,
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = .5f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)  // Adjust padding as needed
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(16.dp),  // Inner padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Dismiss the dialog"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Text "Total to pay:"
                Text(
                    text = "Total to pay:",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Total Price Field
                Text(
                    text = "%.2f".format(totalPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Text "The customer paid a total of:"
                Text(
                    text = "The customer paid a total of:",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Amount Paid Field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = amountPaid,
                    onValueChange = {
                        amountPaid = it
                    },
                    label = { Text("Amount Paid") },
                    isError = !isAmountValid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Text "Change to return:"
                Text(
                    text = "Change to return:",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Change to Return Field
                Text(
                    text = changeToReturn,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Finish Button
                Button(
                    onClick = {
                        // Handle Finish action
                        if (isAmountValid) {
                            invoiceViewModel.addInvoice(
                                userId = userId,
                                totalPrice = totalPrice,
                                paymentMethod = "Cash"
                            )
                            amountPaid = ""
                            mainViewModel.deleteAllItems()
                            onDismiss()
                            navController.navigate("main_screen") {
                                popUpTo("payment_screen") {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Finish")
                }
            }
        }
    }
}
