package com.example.sa4

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: EventViewModel,
    onEventSaved: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val geocoder = Geocoder(context)

    var title      by remember { mutableStateOf("") }
    var details    by remember { mutableStateOf("") }
    var date       by remember { mutableStateOf("") }
    var time       by remember { mutableStateOf("") }
    var address    by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    Column(Modifier.padding(16.dp)) {
        Text("Add/Edit Event")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("Details") }
        )

        // Date picker ...
        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text("Date") },
            readOnly = true,
            modifier = Modifier.padding(top = 8.dp),
            trailingIcon = {
                IconButton(onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            date = "$day/${month + 1}/$year"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                }
            }
        )

        // Time picker ...
        OutlinedTextField(
            value = time,
            onValueChange = {},
            label = { Text("Time") },
            readOnly = true,
            modifier = Modifier.padding(top = 8.dp),
            trailingIcon = {
                IconButton(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, min ->
                            time = String.format("%02d:%02d", hour, min)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Pick Time")
                }
            }
        )

        // Address input
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(onClick = {
            coroutineScope.launch {
                // Do geocoding off the main thread
                val coords = withContext(Dispatchers.IO) {
                    geocoder.getFromLocationName(address, 1)
                        ?.firstOrNull()
                        ?.let { it.latitude to it.longitude }
                        ?: (0.0 to 0.0)
                }

                val (lat, lng) = coords
                val event = Event(
                    name      = title,
                    details   = details,
                    date      = date,
                    time      = time,
                    address   = address,
                    latitude  = lat,
                    longitude = lng
                )

                viewModel.add(event)
                onEventSaved()
            }
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Save")
        }
    }
}
