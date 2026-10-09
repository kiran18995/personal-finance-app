package com.example.financeapp.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.example.financeapp.ui.theme.FinanceAppTheme

@Composable
fun AnimatedGlassBackground(modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()

    // Vibrant Red, Blue, Green, Yellow
    val colorRed = if (isDark) Color(0xFFE63946) else Color(0xFFEF4444)
    val colorBlue = if (isDark) Color(0xFF1D3557) else Color(0xFF3B82F6)
    val colorGreen = if (isDark) Color(0xFF2A9D8F) else Color(0xFF10B981)
    val colorYellow = if (isDark) Color(0xFFE9C46A) else Color(0xFFF59E0B)
    
    // Base background. Light mode gets a very soft off-white to let colors pop.
    val baseBg = if (isDark) Color(0xFF07050F) else Color(0xFFF8FAFC)

    val infiniteTransition = rememberInfiniteTransition(label = "glassAnim")

    // Red
    val p1x by infiniteTransition.animateFloat(
        initialValue = -0.2f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Reverse), label = "x1"
    )
    val p1y by infiniteTransition.animateFloat(
        initialValue = -0.1f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Reverse), label = "y1"
    )

    // Blue
    val p2x by infiniteTransition.animateFloat(
        initialValue = 1.2f, targetValue = -0.2f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing), RepeatMode.Reverse), label = "x2"
    )
    val p2y by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = -0.1f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Reverse), label = "y2"
    )

    // Green
    val p3x by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(16000, easing = LinearEasing), RepeatMode.Reverse), label = "x3"
    )
    val p3y by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(17000, easing = LinearEasing), RepeatMode.Reverse), label = "y3"
    )

    // Yellow
    val p4x by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = -0.1f,
        animationSpec = infiniteRepeatable(tween(19000, easing = LinearEasing), RepeatMode.Reverse), label = "x4"
    )
    val p4y by infiniteTransition.animateFloat(
        initialValue = 1.1f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(21000, easing = LinearEasing), RepeatMode.Reverse), label = "y4"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseBg)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.6f) // Adjust alpha for vibrancy
        ) {
            val radius = size.minDimension * 0.65f

            // Calculate actual pixel positions based on current screen size and animated fractions
            val offset1 = Offset(p1x * size.width, p1y * size.height)
            val offset2 = Offset(p2x * size.width, p2y * size.height)
            val offset3 = Offset(p3x * size.width, p3y * size.height)
            val offset4 = Offset(p4x * size.width, p4y * size.height)

            // Draw Red
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colorRed, Color.Transparent),
                    center = offset1,
                    radius = radius
                ),
                center = offset1,
                radius = radius
            )

            // Draw Blue
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colorBlue, Color.Transparent),
                    center = offset2,
                    radius = radius
                ),
                center = offset2,
                radius = radius
            )

            // Draw Green
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colorGreen, Color.Transparent),
                    center = offset3,
                    radius = radius
                ),
                center = offset3,
                radius = radius
            )

            // Draw Yellow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colorYellow, Color.Transparent),
                    center = offset4,
                    radius = radius
                ),
                center = offset4,
                radius = radius
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "AnimatedGlassBackground Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AnimatedGlassBackgroundLightPreview() {
    FinanceAppTheme { AnimatedGlassBackground() }
}

@Preview(name = "AnimatedGlassBackground Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AnimatedGlassBackgroundDarkPreview() {
    FinanceAppTheme { AnimatedGlassBackground() }
}
