package com.example.sa4

import kotlinx.coroutines.flow.Flow

class EventRepository(private val dao: EventDao) {
    val allEvents: Flow<List<Event>> = dao.getAll()
    suspend fun insert(event: Event) = dao.insert(event)
    suspend fun update(event: Event) = dao.update(event)
    suspend fun delete(event: Event) = dao.delete(event)
}