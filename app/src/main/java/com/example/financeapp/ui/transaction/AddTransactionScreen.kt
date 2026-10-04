package com.example.financeapp.ui.transaction

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onDismiss: () -> Unit,
    onNavigateToChooseAccount: () -> Unit = {},
    onSave: (amount: Double, isIncome: Boolean, category: String, account: String) -> Unit = { _, _, _, _ -> },
    selectedAccount: String? = null
) {
    var selectedType by remember { mutableStateOf("Spent") } // "Spent", "Income", "Transfer"
    var selectedCategory by remember { mutableStateOf("Food") }
    var amount by remember { mutableStateOf("0") }
    val context = LocalContext.current
    val activeAccount = selectedAccount ?: "Primary Bank"

    val isIncome = selectedType == "Income"
    val accentColor = if (isIncome) Emerald else Violet500

    val categories = listOf(
        "Food", "Grocery", "Transport", "Shopping", "Bills",
        "Health", "Coffee", "Gym", "Books", "Travel",
        "Electric", "Internet", "Gift", "Salary", "Invest"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DashboardColors.bg)
    ) {
        
Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DashboardColors.surface)
                        .border(1.dp, DashboardColors.border, CircleShape)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = DashboardColors.textPrimary, modifier = Modifier.size(18.dp))
                }

                Text(
                    text = "New Transaction",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.textPrimary
                )

                IconButton(
                    onClick = {
                        Toast.makeText(context, "Quick shortcut: Tap amounts below to add instantly", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DashboardColors.surface)
                        .border(1.dp, DashboardColors.border, CircleShape)
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "Info", tint = DashboardColors.textSecondary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Segmented Type Selector
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Spent", "Income", "Transfer").forEach { type ->
                        val isSelected = selectedType == type
                        val pillColor = when (type) {
                            "Spent" -> Crimson
                            "Income" -> Emerald
                            else -> Sapphire
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) pillColor else Color.Transparent)
                                .clickable { selectedType = type }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else DashboardColors.textSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Hero Amount Display Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = accentColor.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(22.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isIncome) "INCOME AMOUNT" else "EXPENSE AMOUNT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DashboardColors.textSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "₹",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = amount,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DashboardColors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Add Chips
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(100, 500, 1000, 2000, 5000).forEach { addVal ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DashboardColors.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border),
                                modifier = Modifier.clickable {
                                    val current = amount.toDoubleOrNull() ?: 0.0
                                    amount = (current + addVal).toInt().toString()
                                }
                            ) {
                                Text(
                                    text = "+₹$addVal",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DashboardColors.textSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Select Account Row
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToChooseAccount() },
                shape = RoundedCornerShape(16.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Violet900.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null, tint = Violet400, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Payment Account", fontSize = 11.sp, color = DashboardColors.textSecondary)
                            Text(activeAccount, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Change", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Violet400)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Violet400, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Selector Grid (2 rows)
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    val icon = categoryIcon(category)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedCategory = category },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) Violet700.copy(alpha = 0.8f) else DashboardColors.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Violet400 else DashboardColors.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                icon,
                                contentDescription = category,
                                tint = if (isSelected) Color.White else DashboardColors.textSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = category,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else DashboardColors.textSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Custom Haptic Numpad
            ModernNumpad(
                onNumberClick = { num ->
                    if (amount == "0") amount = num else if (amount.length < 9) amount += num
                },
                onBackspaceClick = {
                    if (amount.length > 1) amount = amount.dropLast(1) else amount = "0"
                },
                onClearClick = {
                    amount = "0"
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm / Save Button
            Button(
                onClick = {
                    val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                    if (parsedAmount <= 0.0) {
                        Toast.makeText(context, "Please enter an amount greater than zero", Toast.LENGTH_SHORT).show()
                    } else {
                        onSave(parsedAmount, isIncome, selectedCategory, activeAccount)
                        Toast.makeText(context, "Transaction recorded!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIncome) Emerald else Violet600
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Save ${if (isIncome) "Income" else "Expense"}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ─── Modern Glass Numpad ─────────────────────────────────────────────
@Composable
private fun ModernNumpad(
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("C", "0", "⌫")
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { key ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                when (key) {
                                    "⌫" -> onBackspaceClick()
                                    "C" -> onClearClick()
                                    else -> onNumberClick(key)
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = DashboardColors.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.surface)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = key,
                                fontSize = if (key == "⌫" || key == "C") 16.sp else 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (key == "⌫" || key == "C") Violet400 else DashboardColors.textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun categoryIcon(category: String): ImageVector {
    return when (category) {
        "Food" -> Icons.Filled.Restaurant
        "Grocery" -> Icons.Filled.ShoppingCart
        "Transport" -> Icons.Filled.DirectionsCar
        "Shopping" -> Icons.Filled.ShoppingBag
        "Bills" -> Icons.Filled.ReceiptLong
        "Health" -> Icons.Filled.Favorite
        "Coffee" -> Icons.Filled.LocalCafe
        "Gym" -> Icons.Filled.FitnessCenter
        "Books" -> Icons.AutoMirrored.Filled.MenuBook
        "Travel" -> Icons.Filled.Flight
        "Electric" -> Icons.Filled.FlashOn
        "Internet" -> Icons.Filled.Wifi
        "Gift" -> Icons.Filled.CardGiftcard
        "Salary" -> Icons.Filled.Payments
        "Invest" -> Icons.Filled.TrendingUp
        else -> Icons.Filled.Category
    }
}
