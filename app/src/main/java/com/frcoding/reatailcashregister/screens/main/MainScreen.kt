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
    val items by viewModel.items.collectAsState(initial = emptyList())

    val totalPrice = items.sumOf { it.price }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, sessionManager = sessionManager, drawerState = drawerState)
        },
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        // Main content of the screen
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                // Define TopBar if needed
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
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
//                        coroutineScope.launch {
//                            itemToEdit = null
//                            isBottomSheetVisible = true
//                            sheetState.expand()
//                        }
//                    },
//                    modifier = Modifier
//                        .padding(16.dp)
//                ) {
//                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Product")
//                }
//            }
        ) {innerPadding ->
            // showing items on main screen
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

//                Text(
//                    text = "Total: ${"%.2f".format(totalPrice)}",
//                    modifier = Modifier
//                        .padding(start = 16.dp),
//                    style = MaterialTheme.typography.bodyMedium
//                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total price text
                    Text(
                        text = "Total: ${"%.2f".format(totalPrice)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Continue button
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Screen.Payment.passTotalPrice("${"%.2f".format(totalPrice)}"))
                        },
                        content = { Text(text = "Continue") }
                    )

                    // Floating Action Button
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
                    viewModel.addItem(updatedItem) // adding new item
                } else {
                    viewModel.updateItem(updatedItem) // updating existing item
                }
                isBottomSheetVisible = false
                itemToEdit = null
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
    val decimalFormat = DecimalFormat("#.00") // Format za cenu sa dva decimalna mesta

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
                Text(text = decimalFormat.format(item.price))
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { onEdit() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
fun BottomSheet(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    viewModel: MainViewModel,
    itemToEdit: Item? = null, // Parametar za uređivanje
    onSave: (Item) -> Unit // Callback za sačuvane promene
) {

    var productName by remember { mutableStateOf(itemToEdit?.name ?: "") }
    var quantity by remember { mutableStateOf(itemToEdit?.quantity ?: "") }
    var priceString by remember { mutableStateOf(itemToEdit?.price?.toString() ?: "") }

    // Launch effect when itemToEdit changes or BottomSheet visibility changes
    LaunchedEffect(itemToEdit, isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            productName = itemToEdit?.name ?: ""
            quantity = itemToEdit?.quantity ?: ""
            priceString = itemToEdit?.price?.toString() ?: ""
        }
    }

    val decimalFormat = DecimalFormat("#.##") // Format za cenu

    fun parsePrice(input: String): Double {
        return input.toDoubleOrNull() ?: 0.0
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
                    .padding(12.dp) // Outer padding
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(24.dp) // Inner padding
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

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            // handle saving
                            val price = parsePrice(priceString)

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

                            // Clear all fields
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

