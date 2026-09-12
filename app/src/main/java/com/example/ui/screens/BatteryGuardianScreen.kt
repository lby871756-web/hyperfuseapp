package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.BoostLog
import com.example.data.system.PowerProfile
import com.example.ui.UiState
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyanNeonGlow
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.LaserLime
import com.example.ui.theme.ObsidianBase
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.PurpleGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BatteryGuardianScreen(
    state: UiState,
    logs: List<BoostLog>,
    onSelectProfile: (PowerProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Battery Profiles Section
        item {
            Text(
                text = stringResource(R.string.battery_profiles_title),
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileCard(
                    title = stringResource(R.string.profile_balanced),
                    description = stringResource(R.string.profile_balanced_desc),
                    icon = Icons.Default.Tune,
                    color = CyanNeon,
                    isSelected = state.powerProfile == PowerProfile.BALANCED,
                    onClick = { onSelectProfile(PowerProfile.BALANCED) }
                )

                ProfileCard(
                    title = stringResource(R.string.profile_saver),
                    description = stringResource(R.string.profile_saver_desc),
                    icon = Icons.Default.Eco,
                    color = LaserLime,
                    isSelected = state.powerProfile == PowerProfile.SAVER,
                    onClick = { onSelectProfile(PowerProfile.SAVER) }
                )

                ProfileCard(
                    title = stringResource(R.string.profile_overdrive),
                    description = stringResource(R.string.profile_overdrive_desc),
                    icon = Icons.Default.RocketLaunch,
                    color = CrimsonAlert,
                    isSelected = state.powerProfile == PowerProfile.OVERDRIVE,
                    onClick = { onSelectProfile(PowerProfile.OVERDRIVE) }
                )
            }
        }

        // Battery Health Diagnostics Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(LaserLime.copy(alpha = 0.3f), ObsidianCardBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = LaserLime, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Lithium Battery Degradation Guard",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "98% HEALTH",
                            color = LaserLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { 0.98f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = LaserLime,
                        trackColor = ObsidianCardBorder
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Voltage Stability: Normal (±5mV)", color = TextMuted, fontSize = 11.sp)
                        Text(text = "Cell Chem: ${state.battery.technology}", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Boost & Optimization History Section
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.battery_history_title),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (logs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_logs_yet),
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(logs, key = { it.id }) { log ->
                BoostLogRow(log)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("profile_${title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.09f) else ObsidianCard
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(
                    if (isSelected) color else ObsidianCardBorder,
                    if (isSelected) color.copy(alpha = 0.4f) else ObsidianCardBorder
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = if (isSelected) 0.25f else 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isSelected) color else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun BoostLogRow(log: BoostLog) {
    val dateStr = SimpleDateFormat("HH:mm:ss • MMM dd", Locale.getDefault()).format(Date(log.timestamp))

    val icon = when (log.eventType) {
        "RAM_PURGE" -> Icons.Default.Bolt
        "COOLDOWN" -> Icons.Default.Shield
        "CHARGE_BOOST" -> Icons.Default.ElectricBolt
        else -> Icons.Default.RocketLaunch
    }

    val iconColor = when (log.eventType) {
        "RAM_PURGE" -> CyanNeon
        "COOLDOWN" -> LaserLime
        "CHARGE_BOOST" -> ElectricAmber
        else -> PurpleGlow
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(iconColor.copy(alpha = 0.2f), ObsidianCardBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = log.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = dateStr, color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }

                Text(text = log.details, color = TextSecondary, fontSize = 11.sp)
            }
        }
    }
}
