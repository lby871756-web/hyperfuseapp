package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.GameItem
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

@Composable
fun HyperPlayScreen(
    state: UiState,
    games: List<GameItem>,
    onToggleTurbo: () -> Unit,
    onSetTargetFps: (Int) -> Unit,
    onToggleTouchBoost: (Boolean) -> Unit,
    onToggleDndShield: (Boolean) -> Unit,
    onRunPingTest: () -> Unit,
    onAddGame: (String, String) -> Unit,
    onRemoveGame: (String) -> Unit,
    onLaunchGame: (GameItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var activeLaunchedGame by remember { mutableStateOf<GameItem?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "hud_pulse")
    val hudGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hud_glow"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Gaming Turbo Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        listOf(
                            if (state.isTurboActive) CyanNeon else PurpleGlow,
                            ObsidianCardBorder
                        )
                    )
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.hyperfuse_hero),
                        contentDescription = "Hyperfuse Hero Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = if (state.isTurboActive) 0.85f else 0.5f
                    )

                    // Cyber Overlay Gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        ObsidianBase.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (state.isTurboActive) CyanNeonGlow else Color.Black.copy(alpha = 0.6f))
                                    .border(1.dp, if (state.isTurboActive) CyanNeon else ObsidianCardBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (state.isTurboActive) stringResource(R.string.gaming_boost_active) else stringResource(R.string.gaming_boost_idle),
                                    color = if (state.isTurboActive) CyanNeon else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Text(
                                text = "${state.targetFps} FPS TARGET",
                                color = LaserLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Column {
                            Text(
                                text = stringResource(R.string.gaming_turbo_title),
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = stringResource(R.string.gaming_turbo_desc),
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Turbo Engagement Main Action Button
        item {
            Button(
                onClick = onToggleTurbo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("turbo_toggle_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isTurboActive) CrimsonAlert else CyanNeon,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = if (state.isTurboActive) Icons.Default.Speed else Icons.Default.RocketLaunch,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.isTurboActive) stringResource(R.string.btn_turbo_disengage) else stringResource(R.string.btn_turbo_engage),
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        // Target FPS Selector
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.2f), ObsidianCardBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.target_fps_label),
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(60, 90, 120, 144).forEach { fps ->
                            val isSelected = state.targetFps == fps
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) CyanNeon else ObsidianBase)
                                    .border(1.dp, if (isSelected) CyanNeon else ObsidianCardBorder, RoundedCornerShape(10.dp))
                                    .clickable { onSetTargetFps(fps) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$fps",
                                    color = if (isSelected) Color.Black else TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // Turbo Toggles: Touch Polling and DND Shield
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.2f), ObsidianCardBorder)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Touch Polling Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TouchApp, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = stringResource(R.string.touch_polling_label), color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = stringResource(R.string.touch_polling_desc), color = TextMuted, fontSize = 10.sp)
                            }
                        }
                        Switch(
                            checked = state.isTouchBoostEnabled,
                            onCheckedChange = onToggleTouchBoost,
                            colors = SwitchDefaults.colors(checkedThumbColor = CyanNeon, checkedTrackColor = CyanNeonGlow)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianCardBorder))
                    Spacer(modifier = Modifier.height(10.dp))

                    // DND Shield Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Block, contentDescription = null, tint = ElectricAmber, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = stringResource(R.string.dnd_shield_label), color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = stringResource(R.string.dnd_shield_desc), color = TextMuted, fontSize = 10.sp)
                            }
                        }
                        Switch(
                            checked = state.isDndShieldEnabled,
                            onCheckedChange = onToggleDndShield,
                            colors = SwitchDefaults.colors(checkedThumbColor = ElectricAmber, checkedTrackColor = ElectricAmber.copy(alpha = 0.2f))
                        )
                    }
                }
            }
        }

        // Network Ping Radar Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = LaserLime, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.ping_radar_title),
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onRunPingTest,
                            enabled = !state.isTestingPing,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LaserLime, contentColor = Color.Black),
                            modifier = Modifier.testTag("ping_test_button")
                        ) {
                            if (state.isTestingPing) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = Color.Black)
                            } else {
                                Text(text = stringResource(R.string.btn_run_ping_test), fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val ping = state.pingMs ?: 28
                    val pingColor = when {
                        ping < 45 -> LaserLime
                        ping < 90 -> ElectricAmber
                        else -> CrimsonAlert
                    }
                    val pingStatus = when {
                        ping < 45 -> stringResource(R.string.ping_status_great)
                        ping < 90 -> stringResource(R.string.ping_status_fair)
                        else -> stringResource(R.string.ping_status_high)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ObsidianBase)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.ping_result, ping),
                                color = pingColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = pingStatus,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(pingColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.NetworkCheck, contentDescription = null, tint = pingColor, modifier = Modifier.size(22.dp))
                        }
                    }
                }
            }
        }

        // Game Space Header & Add Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Gamepad, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.installed_games_header),
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.testTag("add_game_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Game", tint = CyanNeon)
                }
            }
        }

        // Game Items List
        items(games, key = { it.packageName }) { game ->
            GameRowCard(
                game = game,
                onLaunch = {
                    activeLaunchedGame = game
                    onLaunchGame(game)
                    Toast.makeText(context, "Engaged Turbo for ${game.name}!", Toast.LENGTH_SHORT).show()
                },
                onDelete = { onRemoveGame(game.packageName) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Game Dialog
    if (showAddDialog) {
        var gameName by remember { mutableStateOf("") }
        var pkgName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = ObsidianCard,
            title = {
                Text(text = "Add Game to Turbo Space", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = stringResource(R.string.add_game_hint), color = TextMuted, fontSize = 12.sp)

                    OutlinedTextField(
                        value = gameName,
                        onValueChange = { gameName = it },
                        label = { Text("Game Name (e.g. Free Fire)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = ObsidianCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_game_name_input")
                    )

                    OutlinedTextField(
                        value = pkgName,
                        onValueChange = { pkgName = it },
                        label = { Text("Package Name (optional)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = ObsidianCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_game_pkg_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (gameName.isNotBlank()) {
                            val finalPkg = if (pkgName.isNotBlank()) pkgName else "com.game.${gameName.lowercase().replace(" ", "")}"
                            onAddGame(gameName, finalPkg)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black),
                    modifier = Modifier.testTag("confirm_add_game_button")
                ) {
                    Text("Add Game")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Active Turbo Launch Simulation Overlay Modal
    activeLaunchedGame?.let { launched ->
        AlertDialog(
            onDismissRequest = { activeLaunchedGame = null },
            containerColor = ObsidianBase,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = LaserLime)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Turbo Launched: ${launched.name}", color = TextPrimary, fontWeight = FontWeight.Black)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ObsidianCard)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "• CPU Clock Governor: Maximum Performance", color = LaserLime, fontSize = 12.sp)
                    Text(text = "• GPU Rendering Clock: Unlocked (${state.targetFps} FPS)", color = CyanNeon, fontSize = 12.sp)
                    Text(text = "• Touch Sampling: 240Hz Ultra Polling Active", color = ElectricAmber, fontSize = 12.sp)
                    Text(text = "• DND Notification Blocker: ENGAGED", color = TextPrimary, fontSize = 12.sp)
                    Text(text = "• Background Memory Allocation: 85% Locked", color = TextSecondary, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = { activeLaunchedGame = null },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black)
                ) {
                    Text("Close HUD")
                }
            }
        )
    }
}

@Composable
fun GameRowCard(
    game: GameItem,
    onLaunch: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon.copy(alpha = 0.2f), ObsidianCardBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(CyanNeonGlow, PurpleGlow.copy(alpha = 0.3f)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Gamepad,
                        contentDescription = null,
                        tint = CyanNeon,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = game.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "${game.targetFps} FPS",
                            color = LaserLime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "• Touch Boost",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onLaunch,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color.Black),
                    modifier = Modifier.height(36.dp).testTag("launch_game_${game.packageName}")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(R.string.btn_launch_turbo), fontSize = 11.sp, fontWeight = FontWeight.Black)
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Game", tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
