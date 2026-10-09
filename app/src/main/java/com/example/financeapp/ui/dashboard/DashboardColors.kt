package com.example.financeapp.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

@Stable
object DashboardColors {
    // Private immutable color palettes – created once at class load time.
    private val LightBackground = Color(0xFFF8FAFC).copy(alpha = 0.15f)
    private val DarkBackground = Color(0xFF0A0A0F).copy(alpha = 0.3f)

    private val LightSurface = Color(0xFFF8FAFC).copy(alpha = 0.4f)
    private val DarkSurface = Color(0xFF13131A).copy(alpha = 0.4f)

    private val LightGlassBackground = Color.White.copy(alpha = 0.4f)
    private val DarkGlassBackground = Color(0xFF130722).copy(alpha = 0.5f)

    private val LightGlassBorder = Color.Black.copy(alpha = 0.08f)
    private val DarkGlassBorder = Color.White.copy(alpha = 0.16f)

    private val LightBorder = Color(0xFFE2E8F0)
    private val DarkBorder = Color.White.copy(alpha = 0.08f)

    @Composable
    fun bg(): Color = if (isSystemInDarkTheme()) DarkBackground else LightBackground

    @Composable
    fun textPrimary(): Color = if (isSystemInDarkTheme()) Color.White else Color(0xFF0F172A)

    @Composable
    fun textSecondary(): Color = if (isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B)

    @Composable
    fun surface(): Color = if (isSystemInDarkTheme()) DarkSurface else LightSurface

    @Composable
    fun border(): Color = if (isSystemInDarkTheme()) DarkBorder else LightBorder

    @Composable
    fun glassBg(): Color = if (isSystemInDarkTheme()) DarkGlassBackground else LightGlassBackground

    @Composable
    fun glassBorder(): Color = if (isSystemInDarkTheme()) DarkGlassBorder else LightGlassBorder
}
