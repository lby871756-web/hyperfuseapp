package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameItem(
    @PrimaryKey
    val packageName: String,
    val name: String,
    val targetFps: Int = 120,
    val touchBoost: Boolean = true,
    val dndShield: Boolean = true,
    val networkPriority: Boolean = true,
    val lastPlayedTimestamp: Long = 0L
)
