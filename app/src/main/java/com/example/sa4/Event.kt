package com.example.sa4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val details: String,
    val date: String,
    val time: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

