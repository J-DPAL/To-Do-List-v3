package com.example.sa4

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MapScreen(viewModel: EventViewModel) {
    // Observe your events list
    val events by viewModel.allEvents.collectAsState()

    // Decide an initial camera position: use first event if exists, else a default world view
    val initialPosition = events.firstOrNull()?.let {
        LatLng(it.latitude, it.longitude)
    } ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, if (events.isNotEmpty()) 12f else 1f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Place one marker per event (skip placeholder coords)
        events.filter { it.latitude != 0.0 || it.longitude != 0.0 }
            .forEach { event ->
                Marker(
                    state = com.google.maps.android.compose.MarkerState(
                        position = LatLng(event.latitude, event.longitude)
                    ),
                    title = event.name,
                    snippet = "${event.date} @ ${event.time}"
                )
            }
    }
}
