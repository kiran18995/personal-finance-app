package com.example.financeapp.ui.expense

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financeapp.data.Expense
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors
import com.example.financeapp.util.ExpenseCategories
import com.example.financeapp.util.toINR
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(repo: FinanceRepository, onBack: () -> Unit) {
    val expenses by repo.getAllExpenses().collectAsStateWithLifecycle(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val totalExpense = remember(expenses) { expenses.sumOf { it.amount } }

    Scaffold(
        containerColor = DashboardColors.bg,
        topBar = {
            TopAppBar(
                title = { Text("Expenses Tracker", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DashboardColors.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DashboardColors.bg,
                    titleContentColor = DashboardColors.textPrimary,
                    navigationIconContentColor = DashboardColors.textPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Crimson,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.shadow(16.dp, CircleShape, spotColor = Crimson)
            ) {
                Icon(Icons.Default.Add, "Add Expense", modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DashboardColors.bg)
                .padding(padding)
        ) {
            // Ambient glow

Column(modifier = Modifier.fillMaxSize()) {
                // Total Expense Header Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(10.dp, RoundedCornerShape(22.dp), spotColor = Crimson.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(22.dp),
                    color = DashboardColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(CrimsonDim.copy(alpha = 0.45f), DashboardColors.surface)))
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("TOTAL RECORDED SPEND", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Crimson, letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = totalExpense.toINR(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Crimson
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${expenses.size} total expense item(s)", fontSize = 11.sp, color = DashboardColors.textSecondary)
                            }

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonDim)
                                    .border(1.dp, Crimson.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null, tint = Crimson, modifier = Modifier.size(26.dp))
                            }
                        }
                    }
                }

                if (expenses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonDim.copy(alpha = 0.4f))
                                    .border(1.dp, Crimson.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.TrendingDown, null, Modifier.size(36.dp), tint = Crimson)
                            }
                            Text("No expenses recorded yet", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                            Text("Tap the + button to log your daily spending", fontSize = 13.sp, color = DashboardColors.textSecondary)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(expenses, key = { it.id }) { expense ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = DashboardColors.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(CrimsonDim.copy(alpha = 0.4f))
                                                .border(1.dp, Crimson.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(getCategoryEmoji(expense.category), fontSize = 18.sp)
                                        }
                                        Column {
                                            Text(expense.category, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                                            if (expense.description.isNotEmpty()) {
                                                Text(expense.description, fontSize = 12.sp, color = DashboardColors.textSecondary)
                                            }
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = "-${expense.amount.toINR()}",
                                            color = Crimson,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        IconButton(
                                            onClick = { scope.launch { repo.deleteExpense(expense) } },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, "Delete", tint = Crimson.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            onDismiss = { showDialog = false },
            onAdd = { amount, category, desc ->
                scope.launch {
                    repo.addExpense(Expense(amount = amount, category = category, description = desc))
                    showDialog = false
                }
            }
        )
    }
}

@Composable
fun AddExpenseDialog(onDismiss: () -> Unit, onAdd: (Double, String, String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategories[0]) }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DashboardColors.surface,
        shape = RoundedCornerShape(22.dp),
        title = { Text("Add Expense", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)", color = DashboardColors.textSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Crimson,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Crimson,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Expense Category", fontSize = 12.sp, color = DashboardColors.textSecondary, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExpenseCategories.take(3).forEach { cat ->
                        val isSel = selectedCategory == cat
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedCategory = cat },
                            color = if (isSel) Crimson else DashboardColors.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Crimson else DashboardColors.border)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else DashboardColors.textSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Notes / Merchant", color = DashboardColors.textSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Crimson,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Crimson,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val a = amount.toDoubleOrNull() ?: return@Button
                    onAdd(a, selectedCategory, description)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Crimson),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Save Expense", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = DashboardColors.textSecondary) }
        }
    )
}

private fun getCategoryEmoji(category: String): String {
    val c = category.lowercase()
    return when {
        c.contains("food") || c.contains("dining") -> "🍔"
        c.contains("grocery") -> "🛒"
        c.contains("transport") || c.contains("fuel") -> "🚗"
        c.contains("bill") || c.contains("utility") -> "💡"
        c.contains("health") || c.contains("medical") -> "💊"
        c.contains("shopping") -> "🛍️"
        c.contains("entertainment") || c.contains("movie") -> "🎬"
        c.contains("education") -> "📚"
        else -> "💸"
    }
}
