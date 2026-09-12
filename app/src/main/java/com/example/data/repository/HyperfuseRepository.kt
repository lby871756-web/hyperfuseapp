package com.example.data.repository

import com.example.data.db.BoostLog
import com.example.data.db.GameItem
import com.example.data.db.HyperfuseDao
import kotlinx.coroutines.flow.Flow

class HyperfuseRepository(private val dao: HyperfuseDao) {
    val allLogs: Flow<List<BoostLog>> = dao.getAllLogs()
    val allGames: Flow<List<GameItem>> = dao.getAllGames()

    suspend fun logBoost(log: BoostLog) {
        dao.insertLog(log)
    }

    suspend fun clearAllLogs() {
        dao.clearLogs()
    }

    suspend fun saveGame(game: GameItem) {
        dao.insertGame(game)
    }

    suspend fun deleteGame(packageName: String) {
        dao.deleteGame(packageName)
    }

    suspend fun markGameLaunched(packageName: String) {
        dao.updateLastPlayed(packageName, System.currentTimeMillis())
    }
}
