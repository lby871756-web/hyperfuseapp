package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.UiState
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
fun HyperChargeScreen(
    state: UiState,
    onToggleHyperCharge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "reactor_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (state.isHyperChargeActive) 4000 else 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    // Calculate realistic charge ETAs
    val currentPercent = state.battery.percentage
    val isCharging = state.battery.isCharging
    val speedMultiplier = if (state.isHyperChargeActive) 1.35f else 1.0f

    val minutesTo80 = if (currentPercent >= 80) 0 else (((80 - currentPercent) * 0.9f) / speedMultiplier).toInt().coerceAtLeast(1)
    val minutesToFull = if (currentPercent >= 100) 0 else (((100 - currentPercent) * 1.4f) / speedMultiplier).toInt().coerceAtLeast(2)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Reactor Core Charging Hub
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.radialGradient(
                    listOf(
                        if (state.isHyperChargeActive) CyanNeon.copy(alpha = 0.6f) else ElectricAmber.copy(alpha = 0.4f),
                        ObsidianCardBorder
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.charge_boost_title),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (state.isHyperChargeActive) CyanNeonGlow else ElectricAmber.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (state.isHyperChargeActive) "HYPER BOOST ON" else "STANDARD",
                            color = if (state.isHyperChargeActive) CyanNeon else ElectricAmber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Animated Reactor Hub Graphic
                Box(
                    modifier = Modifier.size(190.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(190.dp)
                            .rotate(rotation)
                    ) {
                        val strokeWidth = 5.dp.toPx()
                        val radius = (size.minDimension - strokeWidth) / 2
                        val center = Offset(size.width / 2, size.height / 2)

                        // Outer dashed reactor rings
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = listOf(CyanNeon, ElectricAmber, LaserLime, CyanNeon)
                            ),
                            radius = radius,
                            center = center,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    // Inner glowing circle
                    Box(
                        modifier = Modifier
                            .size(145.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        if (isCharging) CyanNeon.copy(alpha = 0.25f) else ElectricAmber.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(
                                1.5.dp,
                                if (state.isHyperChargeActive) CyanNeon.copy(alpha = 0.5f) else ObsidianCardBorder,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (isCharging) Icons.Default.BatteryChargingFull else Icons.Default.Bolt,
                                contentDescription = null,
                                tint = if (isCharging) CyanNeon else ElectricAmber,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "${currentPercent}%",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isCharging) stringResource(R.string.charge_status_charging) else stringResource(R.string.charge_status_discharging),
                                fontSize = 10.sp,
                                color = if (isCharging) LaserLime else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Charger Type and Current Power
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.charger_type),
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = if (isCharging) state.battery.chargerName else stringResource(R.string.charger_none),
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(ObsidianCardBorder))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Estimated Transfer",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        val estimatedWatts = if (!isCharging) "0.0 W" else if (state.isHyperChargeActive) "33.5 W (Max)" else "18.0 W"
                        Text(
                            text = estimatedWatts,
                            color = if (state.isHyperChargeActive) CyanNeon else LaserLime,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Boost Activation Button
        Button(
            onClick = onToggleHyperCharge,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("hypercharge_toggle_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.isHyperChargeActive) ElectricAmber else CyanNeon,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (state.isHyperChargeActive) stringResource(R.string.btn_disable_hypercharge) else stringResource(R.string.btn_enable_hypercharge),
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ETA Prediction Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.3f), ObsidianCardBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Target 80%", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (currentPercent >= 80) "Reached" else "~$minutesTo80 min",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(text = stringResource(R.string.est_time_80), color = TextSecondary, fontSize = 10.sp)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(LaserLime.copy(alpha = 0.3f), ObsidianCardBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = LaserLime, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Target 100%", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (currentPercent >= 100) "Full" else "~$minutesToFull min",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(text = stringResource(R.string.est_time_full), color = TextSecondary, fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3-Phase Charging Curve Visualizer
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.2f), ObsidianCardBorder)))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = stringResource(R.string.charging_curve_stage),
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                PhaseItem(
                    title = stringResource(R.string.phase_rapid),
                    description = "High constant-current injection for fastest replenishment.",
                    isActive = currentPercent in 0..70,
                    progress = (currentPercent / 70f).coerceIn(0f, 1f),
                    color = CyanNeon
                )

                Spacer(modifier = Modifier.height(10.dp))

                PhaseItem(
                    title = stringResource(R.string.phase_saturation),
                    description = "Constant-voltage regulation to protect battery cells.",
                    isActive = currentPercent in 71..90,
                    progress = if (currentPercent > 70) ((currentPercent - 70) / 20f).coerceIn(0f, 1f) else 0f,
                    color = ElectricAmber
                )

                Spacer(modifier = Modifier.height(10.dp))

                PhaseItem(
                    title = stringResource(R.string.phase_trickle),
                    description = "Micro-pulse trickle to avoid lithium plating degradation.",
                    isActive = currentPercent > 90,
                    progress = if (currentPercent > 90) ((currentPercent - 90) / 10f).coerceIn(0f, 1f) else 0f,
                    color = LaserLime
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PhaseItem(
    title: String,
    description: String,
    isActive: Boolean,
    progress: Float,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) color.copy(alpha = 0.08f) else Color.Transparent)
            .border(1.dp, if (isActive) color.copy(alpha = 0.35f) else Color.Transparent, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = if (isActive) color else TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            if (isActive) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ACTIVE",
                        color = color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Text(
            text = description,
            color = TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = ObsidianCardBorder
        )
    }
}
