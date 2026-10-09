package com.example.financeapp.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financeapp.ui.theme.FinanceAppTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.linearGradient(gradientColors)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            )
            .padding(20.dp),
        content = content
    )
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        gradientColors = listOf(
            color.copy(alpha = 0.15f),
            color.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                androidx.compose.material3.Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(4.dp))
                androidx.compose.material3.Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = color
                )
            }
            icon()
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "GlassCard Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun GlassCardLightPreview() {
    FinanceAppTheme {
        GlassCard(modifier = Modifier.padding(16.dp)) {
            Text("Glass Card Content", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(name = "GlassCard Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun GlassCardDarkPreview() {
    FinanceAppTheme {
        GlassCard(modifier = Modifier.padding(16.dp)) {
            Text("Glass Card Content", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(name = "StatCard Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun StatCardLightPreview() {
    FinanceAppTheme {
        StatCard(
            title = "Total Income",
            value = "₹1,25,000",
            icon = { Icon(Icons.Filled.TrendingUp, null, tint = Color(0xFF10B981)) },
            color = Color(0xFF10B981),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "StatCard Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun StatCardDarkPreview() {
    FinanceAppTheme {
        StatCard(
            title = "Total Income",
            value = "₹1,25,000",
            icon = { Icon(Icons.Filled.TrendingUp, null, tint = Color(0xFF10B981)) },
            color = Color(0xFF10B981),
            modifier = Modifier.padding(16.dp)
        )
    }
}

