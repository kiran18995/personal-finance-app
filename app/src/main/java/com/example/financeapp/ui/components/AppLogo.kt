package com.example.financeapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Blue400
import com.example.financeapp.ui.theme.Blue600
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.FinanceAppTheme
import com.example.financeapp.ui.theme.Ink100
import com.example.financeapp.ui.theme.Ink300

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    showTitle: Boolean = false,
    titleText: String = "My Finance"
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .shadow(
                    elevation = size * 0.25f,
                    shape = RoundedCornerShape(size * 0.32f),
                    spotColor = Blue600.copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(size * 0.32f))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1E1B4B),
                            Color(0xFF0F172A),
                            Color(0xFF0284C7)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(100f, 100f)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(size * 0.32f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size * 0.6f)) {
                val w = this.size.width
                val h = this.size.height

                // 1. Draw minimalist card contour
                val cardWidth = w * 0.85f
                val cardHeight = h * 0.60f
                val left = (w - cardWidth) / 2f
                val top = (h - cardHeight) / 2f

                drawRoundRect(
                    color = Blue400.copy(alpha = 0.45f),
                    topLeft = Offset(left, top),
                    size = Size(cardWidth, cardHeight),
                    cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
                    style = Stroke(width = w * 0.055f)
                )

                // 2. Draw minimalist rising wealth trend path
                val trendPath = Path().apply {
                    moveTo(left + w * 0.08f, top + cardHeight * 0.72f)
                    lineTo(left + cardWidth * 0.35f, top + cardHeight * 0.38f)
                    lineTo(left + cardWidth * 0.58f, top + cardHeight * 0.58f)
                    lineTo(left + cardWidth * 0.92f, top + cardHeight * 0.18f)
                }

                drawPath(
                    path = trendPath,
                    color = Emerald,
                    style = Stroke(
                        width = w * 0.075f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // 3. Peak accent dot
                drawCircle(
                    color = Emerald,
                    radius = w * 0.055f,
                    center = Offset(left + cardWidth * 0.92f, top + cardHeight * 0.18f)
                )
            }
        }

        if (showTitle) {
            Spacer(modifier = Modifier.height(size * 0.2f))
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Ink100,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Smart Personal Vault",
                style = MaterialTheme.typography.bodySmall,
                color = Ink300,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "AppLogo Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "AppLogo Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AppLogoPreview() {
    FinanceAppTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            AppLogo(size = 84.dp, showTitle = true)
        }
    }
}
