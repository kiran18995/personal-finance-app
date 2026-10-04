package com.example.financeapp.ui.dashboard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object DashboardColors {
    val bg @Composable get() = if (isSystemInDarkTheme()) Color(0xFF0A0A0F).copy(alpha = 0.3f) else Color(0xFFF8FAFC).copy(alpha = 0.15f)
    val textPrimary @Composable get() = if (isSystemInDarkTheme()) Color.White else Color(0xFF0F172A)
    val textSecondary @Composable get() = if (isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF64748B)
    val surface @Composable get() = if (isSystemInDarkTheme()) Color(0xFF13131A).copy(alpha = 0.4f) else Color(0xFFF8FAFC).copy(alpha = 0.4f)
    val border @Composable get() = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.08f) else Color(0xFFE2E8F0)
    val glassBg @Composable get() = if (isSystemInDarkTheme()) Color(0xFF130722).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.4f)
    val glassBorder @Composable get() = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.16f) else Color.Black.copy(alpha = 0.08f)
}
