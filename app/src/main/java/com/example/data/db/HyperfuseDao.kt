package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HyperfuseDao {
    @Query("SELECT * FROM boost_logs ORDER BY timestamp DESC LIMIT 50")
    fun getAllLogs(): Flow<List<BoostLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: BoostLog)

    @Query("DELETE FROM boost_logs")
    suspend fun clearLogs()

    @Query("SELECT * FROM games ORDER BY lastPlayedTimestamp DESC, name ASC")
    fun getAllGames(): Flow<List<GameItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameItem)

    @Query("DELETE FROM games WHERE packageName = :packageName")
    suspend fun deleteGame(packageName: String)

    @Query("UPDATE games SET lastPlayedTimestamp = :timestamp WHERE packageName = :packageName")
    suspend fun updateLastPlayed(packageName: String, timestamp: Long)
}
