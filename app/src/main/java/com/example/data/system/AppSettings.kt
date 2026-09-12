package com.example.data.system

import android.content.Context
import android.content.SharedPreferences

enum class PowerProfile {
    BALANCED,
    SAVER,
    OVERDRIVE
}

class AppSettings(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("hyperfuse_prefs", Context.MODE_PRIVATE)

    var language: String
        get() = prefs.getString("key_language", "en") ?: "en"
        set(value) = prefs.edit().putString("key_language", value).apply()

    var tempUnit: String
        get() = prefs.getString("key_temp_unit", "C") ?: "C"
        set(value) = prefs.edit().putString("key_temp_unit", value).apply()

    var hapticEnabled: Boolean
        get() = prefs.getBoolean("key_haptic", true)
        set(value) = prefs.edit().putBoolean("key_haptic", value).apply()

    var chargeAlarmEnabled: Boolean
        get() = prefs.getBoolean("key_charge_alarm", true)
        set(value) = prefs.edit().putBoolean("key_charge_alarm", value).apply()

    var overheatAlertEnabled: Boolean
        get() = prefs.getBoolean("key_overheat_alert", true)
        set(value) = prefs.edit().putBoolean("key_overheat_alert", value).apply()

    var powerProfile: PowerProfile
        get() {
            val name = prefs.getString("key_power_profile", PowerProfile.BALANCED.name)
            return try {
                PowerProfile.valueOf(name ?: PowerProfile.BALANCED.name)
            } catch (_: Exception) {
                PowerProfile.BALANCED
            }
        }
        set(value) = prefs.edit().putString("key_power_profile", value.name).apply()

    var isHyperChargeEnabled: Boolean
        get() = prefs.getBoolean("key_hypercharge_enabled", false)
        set(value) = prefs.edit().putBoolean("key_hypercharge_enabled", value).apply()

    var isTurboModeEnabled: Boolean
        get() = prefs.getBoolean("key_turbo_mode_enabled", false)
        set(value) = prefs.edit().putBoolean("key_turbo_mode_enabled", value).apply()

    var targetFps: Int
        get() = prefs.getInt("key_target_fps", 120)
        set(value) = prefs.edit().putInt("key_target_fps", value).apply()

    var isTouchBoostEnabled: Boolean
        get() = prefs.getBoolean("key_touch_boost", true)
        set(value) = prefs.edit().putBoolean("key_touch_boost", value).apply()

    var isDndShieldEnabled: Boolean
        get() = prefs.getBoolean("key_dnd_shield", true)
        set(value) = prefs.edit().putBoolean("key_dnd_shield", value).apply()
}
