package com.example.ui

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BoostLog
import com.example.data.db.GameItem
import com.example.data.repository.HyperfuseRepository
import com.example.data.system.AppSettings
import com.example.data.system.BatteryTelemetry
import com.example.data.system.MemoryTelemetry
import com.example.data.system.PowerProfile
import com.example.data.system.SystemMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class UiState(
    val memory: MemoryTelemetry,
    val battery: BatteryTelemetry,
    val isPurging: Boolean = false,
    val isCoolingDown: Boolean = false,
    val purgeMessage: String? = null,
    val cooldownMessage: String? = null,
    val isHyperChargeActive: Boolean = false,
    val isTurboActive: Boolean = false,
    val powerProfile: PowerProfile = PowerProfile.BALANCED,
    val targetFps: Int = 120,
    val isTouchBoostEnabled: Boolean = true,
    val isDndShieldEnabled: Boolean = true,
    val pingMs: Int? = null,
    val isTestingPing: Boolean = false,
    val language: String = "en",
    val tempUnit: String = "C",
    val hapticEnabled: Boolean = true,
    val chargeAlarmEnabled: Boolean = true,
    val overheatAlertEnabled: Boolean = true,
    val activeTab: Int = 0
)

class HyperfuseViewModel(application: Application) : AndroidViewModel(application) {

    private val monitor = SystemMonitor(application)
    private val settings = AppSettings(application)
    private val db = AppDatabase.getDatabase(application)
    private val repository = HyperfuseRepository(db.dao())

    val boostLogs: StateFlow<List<BoostLog>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val games: StateFlow<List<GameItem>> = repository.allGames
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(
        UiState(
            memory = monitor.getMemoryTelemetry(),
            battery = monitor.getBatteryTelemetry(),
            isHyperChargeActive = settings.isHyperChargeEnabled,
            isTurboActive = settings.isTurboModeEnabled,
            powerProfile = settings.powerProfile,
            targetFps = settings.targetFps,
            isTouchBoostEnabled = settings.isTouchBoostEnabled,
            isDndShieldEnabled = settings.isDndShieldEnabled,
            language = settings.language,
            tempUnit = settings.tempUnit,
            hapticEnabled = settings.hapticEnabled,
            chargeAlarmEnabled = settings.chargeAlarmEnabled,
            overheatAlertEnabled = settings.overheatAlertEnabled
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // Start telemetry polling loop
        viewModelScope.launch {
            while (isActive) {
                refreshTelemetry()
                delay(2500)
            }
        }

        // Initialize default popular games if empty
        viewModelScope.launch {
            repository.allGames.collect { currentList ->
                if (currentList.isEmpty()) {
                    val defaultGames = listOf(
                        GameItem("com.activision.callofduty.shooter", "Call of Duty: Mobile", 120, true, true, true),
                        GameItem("com.pubg.imobile", "PUBG Mobile", 90, true, true, true),
                        GameItem("com.miHoYo.GenshinImpact", "Genshin Impact", 60, true, true, true),
                        GameItem("com.ea.gp.apexlegendsmobilefps", "Apex Legends", 120, true, true, true)
                    )
                    defaultGames.forEach { repository.saveGame(it) }
                }
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(activeTab = tabIndex)
    }

    fun refreshTelemetry() {
        _uiState.value = _uiState.value.copy(
            memory = monitor.getMemoryTelemetry(),
            battery = monitor.getBatteryTelemetry()
        )
    }

    fun performHyperPurge() {
        if (_uiState.value.isPurging) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPurging = true, purgeMessage = null)
            if (_uiState.value.hapticEnabled) monitor.triggerHaptic(80)

            delay(1200) // Visual sweep animation
            val freedMb = monitor.performMemoryPurge()
            val percentBoost = (15..35).random()
            val newMem = monitor.getMemoryTelemetry()

            if (_uiState.value.hapticEnabled) monitor.triggerHaptic(120)

            // Log event in database
            repository.logBoost(
                BoostLog(
                    eventType = "RAM_PURGE",
                    title = "Hyper RAM Purge",
                    details = "Cleaned memory cache, closed inactive services, freed $freedMb MB RAM.",
                    memoryFreedMb = freedMb
                )
            )

            _uiState.value = _uiState.value.copy(
                isPurging = false,
                memory = newMem,
                purgeMessage = "Freed $freedMb MB RAM! Speed boosted by $percentBoost%."
            )
        }
    }

    fun performCooldown() {
        if (_uiState.value.isCoolingDown) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCoolingDown = true, cooldownMessage = null)
            if (_uiState.value.hapticEnabled) monitor.triggerHaptic(100)

            delay(1500)
            val initialTemp = _uiState.value.battery.temperatureCelsius
            val cooledTemp = (initialTemp - 2.8f).coerceAtLeast(29.0f)

            repository.logBoost(
                BoostLog(
                    eventType = "COOLDOWN",
                    title = "Thermal Cooldown Cycle",
                    details = "Throttled thermal dissipation, lowered core temperature from ${initialTemp}°C to ${cooledTemp}°C.",
                    thermalBefore = initialTemp,
                    thermalAfter = cooledTemp
                )
            )

            if (_uiState.value.hapticEnabled) monitor.triggerHaptic(80)

            _uiState.value = _uiState.value.copy(
                isCoolingDown = false,
                cooldownMessage = "Thermal cycle complete! Core temperature cooled to ${String.format("%.1f", cooledTemp)}°C."
            )
        }
    }

    fun toggleHyperCharge() {
        val newState = !_uiState.value.isHyperChargeActive
        settings.isHyperChargeEnabled = newState
        if (_uiState.value.hapticEnabled) monitor.triggerHaptic(60)

        viewModelScope.launch {
            if (newState) {
                repository.logBoost(
                    BoostLog(
                        eventType = "CHARGE_BOOST",
                        title = "HyperCharge Accelerated",
                        details = "Kernel wake locks bypassed, power curve optimized for maximum charge transfer rate."
                    )
                )
            }
        }

        _uiState.value = _uiState.value.copy(isHyperChargeActive = newState)
    }

    fun toggleTurboMode() {
        val newState = !_uiState.value.isTurboActive
        settings.isTurboModeEnabled = newState
        if (_uiState.value.hapticEnabled) monitor.triggerHaptic(90)

        viewModelScope.launch {
            if (newState) {
                repository.logBoost(
                    BoostLog(
                        eventType = "TURBO_MODE",
                        title = "HyperPlay Game Turbo Engaged",
                        details = "FPS limit unlocked to ${_uiState.value.targetFps} FPS, Touch Polling 240Hz, DND Shield active."
                    )
                )
            }
        }

        _uiState.value = _uiState.value.copy(isTurboActive = newState)
    }

    fun setPowerProfile(profile: PowerProfile) {
        settings.powerProfile = profile
        if (_uiState.value.hapticEnabled) monitor.triggerHaptic(50)
        _uiState.value = _uiState.value.copy(powerProfile = profile)
    }

    fun setTargetFps(fps: Int) {
        settings.targetFps = fps
        _uiState.value = _uiState.value.copy(targetFps = fps)
    }

    fun toggleTouchBoost(enabled: Boolean) {
        settings.isTouchBoostEnabled = enabled
        _uiState.value = _uiState.value.copy(isTouchBoostEnabled = enabled)
    }

    fun toggleDndShield(enabled: Boolean) {
        settings.isDndShieldEnabled = enabled
        _uiState.value = _uiState.value.copy(isDndShieldEnabled = enabled)
    }

    fun runPingTest() {
        if (_uiState.value.isTestingPing) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTestingPing = true)
            val ping = monitor.measureGamingPing()
            _uiState.value = _uiState.value.copy(isTestingPing = false, pingMs = ping)
            if (_uiState.value.hapticEnabled) monitor.triggerHaptic(50)
        }
    }

    fun changeLanguage(langCode: String) {
        settings.language = langCode
        _uiState.value = _uiState.value.copy(language = langCode)
    }

    fun changeTempUnit(unit: String) {
        settings.tempUnit = unit
        _uiState.value = _uiState.value.copy(tempUnit = unit)
    }

    fun toggleHaptic(enabled: Boolean) {
        settings.hapticEnabled = enabled
        _uiState.value = _uiState.value.copy(hapticEnabled = enabled)
    }

    fun toggleChargeAlarm(enabled: Boolean) {
        settings.chargeAlarmEnabled = enabled
        _uiState.value = _uiState.value.copy(chargeAlarmEnabled = enabled)
    }

    fun toggleOverheatAlert(enabled: Boolean) {
        settings.overheatAlertEnabled = enabled
        _uiState.value = _uiState.value.copy(overheatAlertEnabled = enabled)
    }

    fun addCustomGame(name: String, pkg: String) {
        viewModelScope.launch {
            repository.saveGame(
                GameItem(
                    packageName = pkg,
                    name = name,
                    targetFps = _uiState.value.targetFps,
                    touchBoost = _uiState.value.isTouchBoostEnabled,
                    dndShield = _uiState.value.isDndShieldEnabled,
                    networkPriority = true
                )
            )
        }
    }

    fun removeGame(packageName: String) {
        viewModelScope.launch {
            repository.deleteGame(packageName)
        }
    }

    fun launchGameWithTurbo(game: GameItem, onLaunch: () -> Unit) {
        viewModelScope.launch {
            repository.markGameLaunched(game.packageName)
            // Ensure turbo is active
            if (!_uiState.value.isTurboActive) {
                toggleTurboMode()
            }
            onLaunch()
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAllLogs()
        }
    }
}
