package com.example.financeapp.ui.history

import androidx.compose.animation.core.*
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.financeapp.ui.theme.bounceClick
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.graphics.graphicsLayer
import com.example.financeapp.ui.dashboard.RecentTransaction
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    transactions: List<RecentTransaction>,
    onAddTransaction: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") } // All, Income, Expense
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var selectedTxForDetails by remember { mutableStateOf<RecentTransaction?>(null) }

    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val fullDateFormat = remember { SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()) }

    // Summary calculations
    val totalIncome = remember(transactions) {
        transactions.filter { it.isIncome }.sumOf { it.amount }
    }
    val totalExpense = remember(transactions) {
        transactions.filter { !it.isIncome }.sumOf { it.amount }
    }
    val netCashFlow = totalIncome - totalExpense

    // Filtered list
    val filteredTransactions = remember(transactions, searchQuery, selectedFilter, selectedCategoryFilter) {
        transactions.filter { tx ->
            val matchesSearch = searchQuery.isBlank() ||
                    tx.title.contains(searchQuery, ignoreCase = true) ||
                    tx.subtitle.contains(searchQuery, ignoreCase = true) ||
                    tx.account.contains(searchQuery, ignoreCase = true)

            val matchesType = when (selectedFilter) {
                "Income" -> tx.isIncome
                "Expense" -> !tx.isIncome
                else -> true
            }

            val matchesCategory = selectedCategoryFilter == null ||
                    tx.subtitle.contains(selectedCategoryFilter!!, ignoreCase = true) ||
                    tx.title.contains(selectedCategoryFilter!!, ignoreCase = true)

            matchesSearch && matchesType && matchesCategory
        }
    }

    // Grouping by date
    val groupedTransactions = remember(filteredTransactions) {
        filteredTransactions.groupBy { tx ->
            val txCal = Calendar.getInstance().apply { timeInMillis = tx.date }
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

            when {
                isSameDay(txCal, today) -> "Today"
                isSameDay(txCal, yesterday) -> "Yesterday"
                txCal.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR) &&
                        txCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) -> "Earlier this week"
                else -> SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(tx.date))
            }
        }
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    val alpha1 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 100, FastOutSlowInEasing), label = "alpha1")
    val offsetY1 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 100, FastOutSlowInEasing), label = "y1")

    val alpha2 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 200, FastOutSlowInEasing), label = "alpha2")
    val offsetY2 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 200, FastOutSlowInEasing), label = "y2")

    val alpha3 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 300, FastOutSlowInEasing), label = "alpha3")
    val offsetY3 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 300, FastOutSlowInEasing), label = "y3")

    val alpha4 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 400, FastOutSlowInEasing), label = "alpha4")
    val offsetY4 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 400, FastOutSlowInEasing), label = "y4")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DashboardColors.bg())
    ) {
        
Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .graphicsLayer { alpha = alpha1; translationY = offsetY1 },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Transactions",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DashboardColors.textPrimary(),
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "${filteredTransactions.size} records found",
                        fontSize = 12.sp,
                        color = DashboardColors.textSecondary(),
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DashboardColors.surface(),
                        border = BorderStroke(1.dp, DashboardColors.border())
                    ) {
                        IconButton(
                            onClick = onAddTransaction,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Transaction", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DashboardColors.surface(),
                        border = BorderStroke(1.dp, DashboardColors.border())
                    ) {
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Exporting statement CSV...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(Icons.Filled.FileDownload, contentDescription = "Export", tint = Blue400, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Financial Summary Card
            Box(modifier = Modifier.graphicsLayer { alpha = alpha2; translationY = offsetY2 }) {
                SummaryFlowCard(
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    netCashFlow = netCashFlow
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = DashboardColors.surface(),
                border = BorderStroke(1.dp, DashboardColors.border())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = DashboardColors.textSecondary(), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search transactions, accounts, notes...", fontSize = 13.sp, color = DashboardColors.textSecondary()) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = DashboardColors.textPrimary(),
                            unfocusedTextColor = DashboardColors.textPrimary(),
                            cursorColor = Blue400
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear", tint = DashboardColors.textSecondary(), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Filter Chips (All / Income / Expense + Category pills)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Income", "Expense").forEach { type ->
                    val isSelected = selectedFilter == type
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Blue700 else DashboardColors.surface(),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Blue500 else DashboardColors.border()
                        ),
                        modifier = Modifier.clickable { selectedFilter = type }
                    ) {
                        Text(
                            text = type,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else DashboardColors.textSecondary(),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }

                // Category filters
                listOf("Food", "Shopping", "Transport", "Bills", "Health", "Salary", "Investment").forEach { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Sapphire.copy(alpha = 0.8f) else DashboardColors.surface(),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Sapphire else DashboardColors.border()
                        ),
                        modifier = Modifier.clickable {
                            selectedCategoryFilter = if (isSelected) null else cat
                        }
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else DashboardColors.textSecondary(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Grouped Transactions List
            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(DashboardColors.surface())
                                .border(1.dp, DashboardColors.border(), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.SearchOff, contentDescription = null, tint = DashboardColors.textSecondary(), modifier = Modifier.size(32.dp))
                        }
                        Text("No matching transactions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary())
                        Text(
                            "Try clearing your search or removing category filters.",
                            fontSize = 13.sp,
                            color = DashboardColors.textSecondary(),
                            textAlign = TextAlign.Center
                        )
                        if (searchQuery.isNotEmpty() || selectedCategoryFilter != null || selectedFilter != "All") {
                            TextButton(
                                onClick = {
                                    searchQuery = ""
                                    selectedCategoryFilter = null
                                    selectedFilter = "All"
                                }
                            ) {
                                Text("Reset Filters", color = Blue400, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    groupedTransactions.forEach { (dateGroup, txList) ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateGroup,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DashboardColors.textSecondary(),
                                    letterSpacing = 0.3.sp
                                )
                                val groupNet = txList.sumOf { if (it.isIncome) it.amount else -it.amount }
                                Text(
                                    text = "${if (groupNet >= 0) "+" else "-"}₹${String.format("%,.0f", Math.abs(groupNet))}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (groupNet >= 0) Emerald else Crimson
                                )
                            }
                        }

                        items(txList, key = { "${it.id}_${it.date}_${it.title}" }) { tx ->
                            LuxuryTransactionCard(
                                tx = tx,
                                timeStr = timeFormat.format(Date(tx.date)),
                                onClick = { selectedTxForDetails = tx }
                            )
                        }
                    }
                }
            }
        }
    }

    // Detail Bottom Sheet
    selectedTxForDetails?.let { tx ->
        ModalBottomSheet(
            onDismissRequest = { selectedTxForDetails = null },
            containerColor = DashboardColors.surface(),
            contentColor = DashboardColors.textPrimary(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(if (tx.isIncome) EmeraldDim.copy(alpha = 0.5f) else CrimsonDim.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(categoryEmoji(tx.title, tx.isIncome), fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = tx.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.textPrimary()
                )
                Text(
                    text = if (tx.isIncome) "Income Received" else "Payment Made",
                    fontSize = 12.sp,
                    color = DashboardColors.textSecondary()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${if (tx.isIncome) "+" else "-"}₹${String.format("%,.2f", tx.amount)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (tx.isIncome) Emerald else Crimson
                )

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = DashboardColors.surface(),
                    border = BorderStroke(1.dp, DashboardColors.border())
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DetailRow("Category", tx.subtitle)
                        DetailRow("Account", tx.account.ifBlank { "Primary Wallet" })
                        DetailRow("Timestamp", fullDateFormat.format(Date(tx.date)))
                        DetailRow("Status", "Completed", valueColor = Emerald)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Transaction receipt shared!", Toast.LENGTH_SHORT).show()
                        selectedTxForDetails = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Receipt", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color = DashboardColors.textPrimary()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = DashboardColors.textSecondary())
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

// ─── Summary Flow Card ───────────────────────────────────────────────
@Composable
private fun SummaryFlowCard(
    totalIncome: Double,
    totalExpense: Double,
    netCashFlow: Double
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = Blue500.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(20.dp),
        color = DashboardColors.surface(),
        border = BorderStroke(1.dp, DashboardColors.border())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("TOTAL INFLOW", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary(), letterSpacing = 0.8.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text("+₹${String.format("%,.0f", totalIncome)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Emerald)
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(DashboardColors.border())
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text("TOTAL OUTFLOW", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary(), letterSpacing = 0.8.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text("-₹${String.format("%,.0f", totalExpense)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Crimson)
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(DashboardColors.border())
            )

            Column(
                modifier = Modifier.weight(1.1f),
                horizontalAlignment = Alignment.End
            ) {
                Text("NET FLOW", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary(), letterSpacing = 0.8.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${if (netCashFlow >= 0) "+" else "-"}₹${String.format("%,.0f", Math.abs(netCashFlow))}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (netCashFlow >= 0) Emerald else Crimson
                )
            }
        }
    }
}

// ─── Luxury Transaction Card ──────────────────────────────────────────
@Composable
private fun LuxuryTransactionCard(
    tx: RecentTransaction,
    timeStr: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .bounceClick(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = DashboardColors.surface(),
        border = BorderStroke(1.dp, DashboardColors.surface())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Avatar with subtle glowing aura
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (tx.isIncome) EmeraldDim.copy(alpha = 0.35f) else DashboardColors.surface())
                    .border(
                        1.dp,
                        if (tx.isIncome) Emerald.copy(alpha = 0.4f) else DashboardColors.border().copy(alpha = 0.4f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(categoryEmoji(tx.title, tx.isIncome), fontSize = 19.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tx.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DashboardColors.textPrimary(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (tx.account.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DashboardColors.surface()
                        ) {
                            Text(
                                text = tx.account,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Blue300,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = timeStr,
                        fontSize = 11.sp,
                        color = DashboardColors.textSecondary()
                    )
                }
            }

            // Amount
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (tx.isIncome) "+" else "-"}₹${String.format("%,.2f", tx.amount)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (tx.isIncome) Emerald else DashboardColors.textPrimary()
                )
                Text(
                    text = if (tx.isIncome) "Credit" else "Debit",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (tx.isIncome) Emerald.copy(alpha = 0.7f) else Crimson.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// ─── Helpers ──────────────────────────────────────────────────────────
private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun categoryEmoji(title: String, isIncome: Boolean): String {
    if (isIncome) return "💰"
    val t = title.lowercase()
    return when {
        t.contains("food") || t.contains("restaurant") || t.contains("swiggy") || t.contains("zomato") -> "🍔"
        t.contains("grocery") || t.contains("blinkit") || t.contains("zepto") || t.contains("supermarket") -> "🛒"
        t.contains("transport") || t.contains("uber") || t.contains("ola") || t.contains("fuel") || t.contains("petrol") -> "🚗"
        t.contains("shopping") || t.contains("amazon") || t.contains("flipkart") || t.contains("myntra") -> "🛍️"
        t.contains("health") || t.contains("medical") || t.contains("pharmacy") || t.contains("hospital") -> "💊"
        t.contains("movie") || t.contains("netflix") || t.contains("spotify") || t.contains("entertain") -> "🎬"
        t.contains("bill") || t.contains("electric") || t.contains("water") || t.contains("recharge") -> "💡"
        t.contains("loan") || t.contains("emi") || t.contains("mortgage") -> "🏦"
        t.contains("invest") || t.contains("sip") || t.contains("mutual") || t.contains("stock") -> "📈"
        t.contains("salary") || t.contains("bonus") || t.contains("freelance") -> "💼"
        else -> "💳"
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "HistoryScreen Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "HistoryScreen Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HistoryScreenPreview() {
    FinanceAppTheme {
        HistoryScreen(
            transactions = listOf(
                RecentTransaction(id = 1, title = "Salary Credit", subtitle = "Monthly Income", amount = 125000.0, isIncome = true, date = System.currentTimeMillis(), account = "HDFC Bank"),
                RecentTransaction(id = 2, title = "Swiggy Food Order", subtitle = "Food & Dining", amount = 640.0, isIncome = false, date = System.currentTimeMillis(), account = "ICICI Card"),
                RecentTransaction(id = 3, title = "Uber Ride", subtitle = "Cab Fare", amount = 320.0, isIncome = false, date = System.currentTimeMillis(), account = "HDFC Bank")
            )
        )
    }
}


