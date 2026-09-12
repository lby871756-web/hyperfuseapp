package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boost_logs")
data class BoostLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventType: String, // RAM_PURGE, COOLDOWN, CHARGE_BOOST, TURBO_SESSION
    val title: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis(),
    val memoryFreedMb: Int = 0,
    val thermalBefore: Float = 0f,
    val thermalAfter: Float = 0f
)
