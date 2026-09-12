package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.UiState
import com.example.ui.components.CyberDialGauge
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyanNeonGlow
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.LaserLime
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    state: UiState,
    onHyperPurge: () -> Unit,
    onCooldown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // RAM Dial Gauge Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.4f), ObsidianCardBorder)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            tint = CyanNeon,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.ram_status_title),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (state.memory.isLowMemory) CrimsonAlert.copy(alpha = 0.2f) else CyanNeonGlow)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (state.memory.isLowMemory) "RAM CRITICAL" else "OPTIMAL",
                            color = if (state.memory.isLowMemory) CrimsonAlert else CyanNeon,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CyberDialGauge(
                    percentage = state.memory.usedPercentage,
                    label = stringResource(R.string.ram_used_label),
                    subLabel = "${state.memory.usedRamMb} MB / ${state.memory.totalRamMb} MB"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.ram_available_label),
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${state.memory.availRamMb} MB",
                            color = LaserLime,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(28.dp)
                            .background(ObsidianCardBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.ram_total_label),
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${state.memory.totalRamMb} MB",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Action Buttons
        Button(
            onClick = onHyperPurge,
            enabled = !state.isPurging,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .scale(if (state.isPurging) 1f else pulseScale)
                .testTag("hyper_purge_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CyanNeon,
                contentColor = Color.Black
            )
        ) {
            if (state.isPurging) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.Black,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.purging_ram),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.btn_hyper_purge),
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onCooldown,
            enabled = !state.isCoolingDown,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("cooling_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = CyanNeon
            ),
            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon, ElectricAmber)))
        ) {
            if (state.isCoolingDown) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = CyanNeon,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cooling Hardware…",
                    fontSize = 13.sp,
                    color = CyanNeon
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = null,
                    tint = CyanNeon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.btn_cooling),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Notification / Feedback Banner
        AnimatedVisibility(
            visible = state.purgeMessage != null || state.cooldownMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val message = state.purgeMessage ?: state.cooldownMessage.orEmpty()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LaserLime.copy(alpha = 0.15f)),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(LaserLime, LaserLime.copy(alpha = 0.3f))))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = LaserLime,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = message,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // System Telemetry Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.device_telemetry),
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Temperature Card
            val tempC = state.battery.temperatureCelsius
            val displayTemp = if (state.tempUnit == "F") {
                "${String.format("%.1f", (tempC * 9 / 5) + 32)}°F"
            } else {
                "${String.format("%.1f", tempC)}°C"
            }
            val tempColor = when {
                tempC < 37f -> LaserLime
                tempC < 42f -> ElectricAmber
                else -> CrimsonAlert
            }

            TelemetryCard(
                icon = Icons.Default.Thermostat,
                title = stringResource(R.string.thermal_status),
                value = displayTemp,
                subtext = when {
                    tempC < 37f -> stringResource(R.string.thermal_optimal)
                    tempC < 42f -> stringResource(R.string.thermal_warm)
                    else -> stringResource(R.string.thermal_hot)
                },
                accentColor = tempColor,
                modifier = Modifier.weight(1f)
            )

            // Battery Level Card
            TelemetryCard(
                icon = Icons.Default.ElectricBolt,
                title = stringResource(R.string.power_state),
                value = "${state.battery.percentage}%",
                subtext = if (state.battery.isCharging) state.battery.chargerName else "Discharging",
                accentColor = if (state.battery.isCharging) CyanNeon else ElectricAmber,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Voltage Card
            TelemetryCard(
                icon = Icons.Default.Speed,
                title = stringResource(R.string.system_voltage),
                value = "${state.battery.voltageMv} mV",
                subtext = state.battery.technology,
                accentColor = CyanNeon,
                modifier = Modifier.weight(1f)
            )

            // Battery Health Card
            TelemetryCard(
                icon = Icons.Default.CheckCircle,
                title = stringResource(R.string.battery_health_status),
                value = state.battery.healthString,
                subtext = "Lithium Monitored",
                accentColor = LaserLime,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TelemetryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    subtext: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(accentColor.copy(alpha = 0.3f), ObsidianCardBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtext,
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
