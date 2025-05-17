package com.example.sa4

import android.location.Geocoder
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ListScreen(viewModel: EventViewModel, navController: NavController) {
    val events by viewModel.allEvents.collectAsState()
    val context = LocalContext.current

    Column(
        Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        navController.navigate("calc")
                    }
                )
            }
    ) {
        Text("Event List")
        events.forEach { event ->
            var isChecked by remember { mutableStateOf(false) }
            var isEditing by remember { mutableStateOf(false) }

            var newName by remember { mutableStateOf(event.name) }
            var newDetails by remember { mutableStateOf(event.details) }
            var newDate by remember { mutableStateOf(event.date) }
            var newTime by remember { mutableStateOf(event.time) }
            var newAddress by remember { mutableStateOf(event.address) }
            var newLatitude by remember { mutableStateOf(event.latitude.toString()) }
            var newLongitude by remember { mutableStateOf(event.longitude.toString()) }

            Card(Modifier.padding(8.dp)) {
                Column(Modifier.padding(16.dp)) {
                    if (isEditing) {
                        OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Name") })
                        OutlinedTextField(value = newDetails, onValueChange = { newDetails = it }, label = { Text("Details") })
                        OutlinedTextField(value = newDate, onValueChange = { newDate = it }, label = { Text("Date") })
                        OutlinedTextField(value = newTime, onValueChange = { newTime = it }, label = { Text("Time") })
                        OutlinedTextField(value = newAddress, onValueChange = { newAddress = it }, label = { Text("Address") })
                        OutlinedTextField(value = newLatitude, onValueChange = { newLatitude = it }, label = { Text("Latitude") })
                        OutlinedTextField(value = newLongitude, onValueChange = { newLongitude = it }, label = { Text("Longitude") })

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = {
                                val geocoder = Geocoder(context)
                                val location = geocoder.getFromLocationName(newAddress, 1)?.firstOrNull()
                                val lat = location?.latitude ?: event.latitude
                                val lon = location?.longitude ?: event.longitude

                                viewModel.edit(
                                    event,
                                    newName,
                                    newDetails,
                                    newDate,
                                    newTime,
                                    newAddress,
                                    lat,
                                    lon
                                )
                                isEditing = false
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Save Edit")
                            }
                        }
                    } else {
                        Text("${event.name}\n${event.date} at ${event.time}\n${event.address}\n(${event.latitude}, ${event.longitude})")
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = {
                                    isChecked = it
                                    if (it) {
                                        viewModel.remove(event)
                                    }
                                }
                            )
                            IconButton(onClick = {
                                isEditing = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                        }
                    }
                }
            }
        }

        Button(onClick = {
            NotificationHelper.notify(
                context = context,
                id = 999,
                title = "Test Notification",
                message = "This is a test notification!"
            )
        }) {
            Text("Send Test Notification")
        }
    }
}
