package com.example.sa4

import ads_mobile_sdk.h6
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        NotificationHelper.createChannel(context)
    }


    val items = listOf("list", "add", "map")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Navigation", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                items.forEach { route ->
                    TextButton(onClick = {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    }) {
                        Text(route.replaceFirstChar { it.uppercase() })
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SA4 To-Do App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            selected = navController.currentBackStackEntryAsState().value?.destination?.route == screen,
                            onClick = { navController.navigate(screen) },
                            icon = {
                                when (screen) {
                                    "list" -> Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List")
                                    "add" -> Icon(Icons.Default.Add, contentDescription = "Add")
                                    "map" -> Icon(Icons.Default.Place, contentDescription = "Map")
                                }
                            },
                            label = { Text(screen.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            val eventViewModel: EventViewModel = viewModel(EventViewModel::class.java)

            NavHost(navController, startDestination = "list", Modifier.padding(innerPadding)) {
                composable("list") { ListScreen(eventViewModel) }
                composable("add") { AddEditScreen(viewModel = eventViewModel, onEventSaved = { navController.navigate("list") }) }
                composable("map") {
                    MapScreen(viewModel = eventViewModel)
                }

            }


        }
    }
}