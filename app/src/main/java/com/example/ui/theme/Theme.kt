package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val HyperfuseColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = ObsidianBase,
    primaryContainer = CyanNeonGlow,
    onPrimaryContainer = CyanNeon,
    secondary = ElectricAmber,
    onSecondary = ObsidianBase,
    tertiary = LaserLime,
    onTertiary = ObsidianBase,
    background = ObsidianBase,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianCardBorder,
    error = CrimsonAlert,
    onError = TextPrimary
)

@Composable
fun HyperfuseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HyperfuseColorScheme,
        typography = Typography,
        content = content
    )
}
