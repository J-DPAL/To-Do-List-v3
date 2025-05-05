package com.example.sa4

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repo = EventRepository(EventDatabase.getDatabase(context).eventDao())

    val allEvents: StateFlow<List<Event>> = repo.allEvents
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(event: Event) = viewModelScope.launch {
        repo.insert(event)
        NotificationHelper.notify(
            context,
            event.id,
            "Event Created",
            "Created: ${event.name} on ${event.date} at ${event.time}"
        )
    }

    fun edit(
        original: Event,
        newName: String,
        newDetails: String,
        newDate: String,
        newTime: String,
        newAddress: String,
        newLatitude: Double,
        newLongitude: Double
    ) = viewModelScope.launch {
        val updated = original.copy(
            name = newName,
            details = newDetails,
            date = newDate,
            time = newTime,
            address = newAddress,
            latitude = newLatitude,
            longitude = newLongitude
        )
        repo.update(updated)

        // Logging for debug
        Log.d("EventViewModel", "Sending UPDATE notification for: ${updated.name}")

        NotificationHelper.notify(
            context,
            updated.id,
            "Event Updated",
            "Updated: ${updated.name} on ${updated.date} at ${updated.time}"
        )
    }

    fun remove(event: Event) = viewModelScope.launch {
        NotificationHelper.notify(
            context,
            event.id,
            "Event Deleted",
            "Deleted: ${event.name} on ${event.date} at ${event.time}"
        )
        repo.delete(event)
    }
}
