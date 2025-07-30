package com.frcoding.reatailcashregister.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.models.Item
import com.frcoding.reatailcashregister.screens.Screen
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    sessionManager: SessionManager,
    viewModel: MainViewModel = hiltViewModel()
) {

    val drawerState = remember { DrawerState(initialValue = DrawerValue.Closed) }
    val coroutineScope = rememberCoroutineScope()
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<Item?>(null) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var items by remember { mutableStateOf(emptyList<Item>()) }
    val itemsFlow = viewModel.items.collectAsState(initial = emptyList())
    items = itemsFlow.value

    var discount by rememberSaveable { mutableStateOf(0.0) }

    val totalPrice = items.sumOf {
        val quantity = it.quantity.toDoubleOrNull() ?: 0.0
        val discountedPrice = it.price * (1 - discount / 100)
        discountedPrice * quantity
    }

    fun applyDiscount(newDiscount: Double) {
        discount = newDiscount
    }

    val discountSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var isDiscountSheetVisible by rememberSaveable { mutableStateOf(false) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, sessionManager = sessionManager, drawerState = drawerState)
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Cash Register", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Open Drawer",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.Black
                    )
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(items) { item ->
                        ItemCard(
                            item = item,
                            onEdit = {
                                itemToEdit = item
                                coroutineScope.launch {
                                    isBottomSheetVisible = true
                                    sheetState.expand()
                                }
                            },
                            onDelete = {
                                viewModel.deleteItem(item)
                            }
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Total: ${"%.2f".format(totalPrice)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        isDiscountSheetVisible = true
                                        sheetState.expand()
                                    }
                                },
                                content = { Text(text = "Discount") }
                            )

                            OutlinedButton(
                                onClick = {
                                    navController.navigate(Screen.Payment.passTotalPrice("${"%.2f".format(totalPrice)}"))
                                },
                                content = { Text(text = "Continue") }
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    coroutineScope.launch {
                                        itemToEdit = null
                                        isBottomSheetVisible = true
                                        sheetState.expand()
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Product")
                            }
                        }
                    }
                }
            }
        }

        BottomSheet(
            isBottomSheetVisible = isBottomSheetVisible,
            sheetState = sheetState,
            onDismiss = {
                isBottomSheetVisible = false
                itemToEdit = null
            },
            viewModel = viewModel,
            itemToEdit = itemToEdit,
            onSave = {updatedItem ->
                if (itemToEdit == null) {
                    viewModel.addItem(updatedItem)
                } else {
                    viewModel.updateItem(updatedItem)
                }
                isBottomSheetVisible = false
                itemToEdit = null
            }
        )

        DiscountBottomSheet(
            isDiscountSheetVisible = isDiscountSheetVisible,
            sheetState = discountSheetState,
            onDismiss = { isDiscountSheetVisible = false },
            onApplyDiscount = { newDiscount ->
                applyDiscount(newDiscount)
                isDiscountSheetVisible = false
            }
        )
    }
}

@Composable
fun ItemCard(
    item: Item,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val quantity = item.quantity.toDoubleOrNull() ?: 0.0
    val totalPriceItem = item.price * quantity
    val decimalFormat = DecimalFormat("#.00")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = "Quantity:", fontWeight = FontWeight.Bold)
                Text(text = item.quantity)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = "Price:", fontWeight = FontWeight.Bold)
                Text(text = decimalFormat.format(totalPriceItem))
            }

            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                IconButton(onClick = { onEdit() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }

            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountBottomSheet(
    isDiscountSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApplyDiscount: (Double) -> Unit
) {
    var discountInput by remember { mutableStateOf("") }

    if (isDiscountSheetVisible) {
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
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Enter Discount (%)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = discountInput,
                    onValueChange = { discountInput = it },
                    label = { Text("Discount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = {
                        // Hide keyboard when done
                    }),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val discount = discountInput.toDoubleOrNull() ?: 0.0
                        onApplyDiscount(discount)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply")
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    viewModel: MainViewModel,
    itemToEdit: Item? = null,
    onSave: (Item) -> Unit,
) {

    var productName by remember { mutableStateOf(itemToEdit?.name ?: "") }
    var quantity by remember { mutableStateOf(itemToEdit?.quantity ?: "") }
    var priceString by remember { mutableStateOf(itemToEdit?.price?.toString() ?: "") }

    val price = priceString.toDoubleOrNull() ?: 0.0
    val quantityDouble = quantity.toDoubleOrNull() ?: 0.0
    val totalPrice = price * quantityDouble
    val formattedPrice = String.format("%.2f", totalPrice)

    LaunchedEffect(itemToEdit, isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            productName = itemToEdit?.name ?: ""
            quantity = itemToEdit?.quantity?.toString() ?: ""
            priceString = itemToEdit?.price?.toString() ?: ""
        }
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
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                FilledIconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = onDismiss,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Dismiss the dialog."
                    )
                }
            }

            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(12.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text(text = "Product Name") },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = priceString,
                    onValueChange = { priceString = it },
                    label = { Text("Price") },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Total Price: $formattedPrice",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            val updatedItem = itemToEdit?.copy(
                                name = productName,
                                quantity = quantity,
                                price = price
                            ) ?: Item (
                                name = productName,
                                quantity = quantity,
                                price = price
                            )

                            onSave(updatedItem)

                            productName = ""
                            quantity = ""
                            priceString = ""

                            onDismiss()
                        },
                        content = { Text(text = "Save") }
                    )
                }
            }
        }

    }
}



@Composable
fun DrawerContent(
    navController: NavController,
    sessionManager: SessionManager,
    drawerState: DrawerState,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(text = "Welcome!", fontSize = 50.sp, fontWeight = FontWeight.SemiBold)
        }

        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Home") },
            selected = false,
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "home")},
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                }
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Main.route) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationDrawerItem(
            label = { Text(text = "My invoices") },
            selected = false,
            icon = { Icon(imageVector = Icons.Default.List, contentDescription = "my_invoices")},
            onClick = {
                coroutineScope.launch {
                    drawerState.close()
                }
                navController.navigate(Screen.InvoicesReview.route) {
                    popUpTo(Screen.InvoicesReview.route) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationDrawerItem(
            label = { Text(text = "Logout") },
            selected = false,
            icon = { Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "logout")},
            onClick = {
                coroutineScope.launch {
                    drawerState.close()

                    mainViewModel.deleteAllItems()

                    sessionManager.clearSession()

                    navController.navigate("sign_in_screen"){
                        popUpTo("sign_in_screen") {
                            inclusive = true
                        }
                    }
                }

            }
        )
    }
}

