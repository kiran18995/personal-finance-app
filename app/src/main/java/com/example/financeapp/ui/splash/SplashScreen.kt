package com.example.financeapp.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.example.financeapp.ui.theme.FinanceAppTheme
import com.example.financeapp.ui.components.AppLogo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        onSplashFinished()
    }

    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF0A0A0F) else Color.White
    val textPrimary = if (isDark) Color.White else Color(0xFF0A0A0F)
    val glowColor1 = Color(0xFF1D4ED8)
    val glowColor2 = Color(0xFF3B82F6)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        // Glowing background orb
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor1.copy(alpha = 0.4f * alphaAnim.value), Color.Transparent),
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.width * 0.8f * scaleAnim.value
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor2.copy(alpha = 0.3f * alphaAnim.value), Color.Transparent),
                    center = Offset(size.width / 2, size.height / 2 + 200f),
                    radius = size.width * 0.9f * scaleAnim.value
                )
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                alpha = alphaAnim.value
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
        ) {
            AppLogo(size = 92.dp, showTitle = true, titleText = "My Finance")
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "Splash Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SplashScreenLightPreview() {
    FinanceAppTheme { SplashScreen(onSplashFinished = {}) }
}

@Preview(name = "Splash Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SplashScreenDarkPreview() {
    FinanceAppTheme { SplashScreen(onSplashFinished = {}) }
}
