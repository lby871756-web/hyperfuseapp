package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.UiState
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyanNeonGlow
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.LaserLime
import com.example.ui.theme.ObsidianBase
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    state: UiState,
    onChangeLanguage: (String) -> Unit,
    onChangeTempUnit: (String) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onToggleChargeAlarm: (Boolean) -> Unit,
    onToggleOverheatAlert: (Boolean) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Language Selector Card (English, Arabic, French)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.3f), ObsidianCardBorder)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.settings_language),
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val languages = listOf(
                        Triple("en", "English", "EN"),
                        Triple("ar", "العربية", "AR"),
                        Triple("fr", "Français", "FR")
                    )

                    languages.forEach { (code, title, tag) ->
                        val isSelected = state.language == code
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CyanNeon else ObsidianBase)
                                .border(1.dp, if (isSelected) CyanNeon else ObsidianCardBorder, RoundedCornerShape(10.dp))
                                .clickable { onChangeLanguage(code) }
                                .testTag("lang_button_$code"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = title,
                                    color = if (isSelected) Color.Black else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = tag,
                                    color = if (isSelected) Color.Black.copy(alpha = 0.7f) else TextMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // Temperature Unit Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(ElectricAmber.copy(alpha = 0.2f), ObsidianCardBorder)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Thermostat, contentDescription = null, tint = ElectricAmber, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.settings_temp_unit),
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("C" to stringResource(R.string.temp_celsius), "F" to stringResource(R.string.temp_fahrenheit)).forEach { (unit, title) ->
                        val isSelected = state.tempUnit == unit
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricAmber else ObsidianBase)
                                .border(1.dp, if (isSelected) ElectricAmber else ObsidianCardBorder, RoundedCornerShape(10.dp))
                                .clickable { onChangeTempUnit(unit) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) Color.Black else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Toggles & Preferences Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.2f), ObsidianCardBorder)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Haptics Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Vibration, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = stringResource(R.string.settings_haptics), color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = stringResource(R.string.settings_haptics_desc), color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = state.hapticEnabled,
                        onCheckedChange = onToggleHaptics,
                        colors = SwitchDefaults.colors(checkedThumbColor = CyanNeon, checkedTrackColor = CyanNeonGlow)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianCardBorder))
                Spacer(modifier = Modifier.height(10.dp))

                // Charge Complete Alarm
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = LaserLime, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = stringResource(R.string.settings_charge_alarm), color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = stringResource(R.string.settings_charge_alarm_desc), color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = state.chargeAlarmEnabled,
                        onCheckedChange = onToggleChargeAlarm,
                        colors = SwitchDefaults.colors(checkedThumbColor = LaserLime, checkedTrackColor = LaserLime.copy(alpha = 0.2f))
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianCardBorder))
                Spacer(modifier = Modifier.height(10.dp))

                // Overheat Warning
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = CrimsonAlert, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = stringResource(R.string.settings_high_temp_alert), color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = stringResource(R.string.settings_high_temp_alert_desc), color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = state.overheatAlertEnabled,
                        onCheckedChange = onToggleOverheatAlert,
                        colors = SwitchDefaults.colors(checkedThumbColor = CrimsonAlert, checkedTrackColor = CrimsonAlert.copy(alpha = 0.2f))
                    )
                }
            }
        }

        // Clear Data Button
        Button(
            onClick = {
                onClearHistory()
                Toast.makeText(context, "Optimization history cleared!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("clear_data_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ObsidianCard, contentColor = CrimsonAlert)
        ) {
            Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = CrimsonAlert, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.settings_clear_data), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // About Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.25f), ObsidianCardBorder)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.about_app), color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = stringResource(R.string.about_version), color = CyanNeon, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = stringResource(R.string.about_desc), color = TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
