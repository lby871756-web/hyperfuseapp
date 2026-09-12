package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.HyperfuseViewModel
import com.example.ui.screens.BatteryGuardianScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HyperChargeScreen
import com.example.ui.screens.HyperPlayScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyanNeonGlow
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.LaserLime
import com.example.ui.theme.ObsidianBase
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyperfuseMainContent(
    viewModel: HyperfuseViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val boostLogs by viewModel.boostLogs.collectAsState()
    val games by viewModel.games.collectAsState()

    val layoutDirection = if (state.language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    // Update Locale in configuration if needed
    val config = LocalConfiguration.current
    val targetLocale = Locale(state.language)
    config.setLocale(targetLocale)

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = ObsidianBase,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyanNeonGlow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = CyanNeon,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "HYPERFUSE",
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                letterSpacing = 1.5.sp,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "CHARGE & PLAY",
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CyanNeon
                            )
                        }
                    },
                    actions = {
                        // Quick status badge (Turbo / HyperCharge indicator)
                        if (state.isTurboActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CyanNeon)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "TURBO ON",
                                    color = Color.Black,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        if (state.isHyperChargeActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ElectricAmber)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "BOOST",
                                    color = Color.Black,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ObsidianSurface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = ObsidianSurface,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = state.activeTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        icon = { Icon(Icons.Default.Memory, contentDescription = stringResource(R.string.tab_dashboard)) },
                        label = { Text(text = stringResource(R.string.tab_dashboard), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = CyanNeon,
                            indicatorColor = CyanNeon,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_overview")
                    )

                    NavigationBarItem(
                        selected = state.activeTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        icon = { Icon(Icons.Default.Bolt, contentDescription = stringResource(R.string.tab_charge)) },
                        label = { Text(text = stringResource(R.string.tab_charge), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = ElectricAmber,
                            indicatorColor = ElectricAmber,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_charge")
                    )

                    NavigationBarItem(
                        selected = state.activeTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        icon = { Icon(Icons.Default.Gamepad, contentDescription = stringResource(R.string.tab_gaming)) },
                        label = { Text(text = stringResource(R.string.tab_gaming), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = CyanNeon,
                            indicatorColor = CyanNeon,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_gaming")
                    )

                    NavigationBarItem(
                        selected = state.activeTab == 3,
                        onClick = { viewModel.selectTab(3) },
                        icon = { Icon(Icons.Default.Shield, contentDescription = stringResource(R.string.tab_battery)) },
                        label = { Text(text = stringResource(R.string.tab_battery), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = LaserLime,
                            indicatorColor = LaserLime,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_battery")
                    )

                    NavigationBarItem(
                        selected = state.activeTab == 4,
                        onClick = { viewModel.selectTab(4) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.tab_settings)) },
                        label = { Text(text = stringResource(R.string.tab_settings), fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = TextPrimary,
                            indicatorColor = ObsidianCardBorder,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_settings")
                    )
                }
            }
        ) { innerPadding ->
            Crossfade(
                targetState = state.activeTab,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    0 -> DashboardScreen(
                        state = state,
                        onHyperPurge = { viewModel.performHyperPurge() },
                        onCooldown = { viewModel.performCooldown() }
                    )
                    1 -> HyperChargeScreen(
                        state = state,
                        onToggleHyperCharge = { viewModel.toggleHyperCharge() }
                    )
                    2 -> HyperPlayScreen(
                        state = state,
                        games = games,
                        onToggleTurbo = { viewModel.toggleTurboMode() },
                        onSetTargetFps = { viewModel.setTargetFps(it) },
                        onToggleTouchBoost = { viewModel.toggleTouchBoost(it) },
                        onToggleDndShield = { viewModel.toggleDndShield(it) },
                        onRunPingTest = { viewModel.runPingTest() },
                        onAddGame = { name, pkg -> viewModel.addCustomGame(name, pkg) },
                        onRemoveGame = { pkg -> viewModel.removeGame(pkg) },
                        onLaunchGame = { game -> viewModel.launchGameWithTurbo(game) {} }
                    )
                    3 -> BatteryGuardianScreen(
                        state = state,
                        logs = boostLogs,
                        onSelectProfile = { viewModel.setPowerProfile(it) }
                    )
                    4 -> SettingsScreen(
                        state = state,
                        onChangeLanguage = { viewModel.changeLanguage(it) },
                        onChangeTempUnit = { viewModel.changeTempUnit(it) },
                        onToggleHaptics = { viewModel.toggleHaptic(it) },
                        onToggleChargeAlarm = { viewModel.toggleChargeAlarm(it) },
                        onToggleOverheatAlert = { viewModel.toggleOverheatAlert(it) },
                        onClearHistory = { viewModel.clearAllHistory() }
                    )
                }
            }
        }
    }
}
