// src/main/java/com/example/financeapp/ui/common/AmountText.kt
package com.example.financeapp.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Crimson
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.FinanceAppTheme
import com.example.financeapp.util.toINR

@Suppress("ktlint:standard:function-naming", "ktlint:standard:trailing-comma-on-declaration-site")
@Composable
fun AmountText(
    amount: Double,
    isIncome: Boolean
) {
    val sign = if (isIncome) "+" else "-"
    val color = if (isIncome) Emerald else Crimson
    Text(
        text = "$sign${amount.toINR()}",
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.End,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview(name = "AmountText Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun AmountTextLightPreview() {
    FinanceAppTheme {
        AmountText(amount = 1234.56, isIncome = true)
    }
}

@Preview(name = "AmountText Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AmountTextDarkPreview() {
    FinanceAppTheme {
        AmountText(amount = 1234.56, isIncome = false)
    }
}
