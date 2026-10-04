package com.example.financeapp.ui.investment

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
import androidx.compose.material.icons.automirrored.filled.ShowChart
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
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.Investment
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors
import com.example.financeapp.util.InvestmentTypes
import com.example.financeapp.util.toINR
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentScreen(repo: FinanceRepository, onBack: () -> Unit) {
    val investments by repo.getAllInvestments().collectAsStateWithLifecycle(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val totalInvested = remember(investments) { investments.sumOf { it.investedAmount } }
    val totalCurrent = remember(investments) { investments.sumOf { it.currentValue } }
    val totalGain = totalCurrent - totalInvested
    val returnPercent = if (totalInvested > 0) (totalGain / totalInvested) * 100 else 0.0

    Scaffold(
        containerColor = DashboardColors.bg,
        topBar = {
            TopAppBar(
                title = { Text("Wealth & Portfolio", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
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
                containerColor = Sapphire,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.shadow(16.dp, CircleShape, spotColor = Sapphire)
            ) {
                Icon(Icons.Default.Add, "Add Asset", modifier = Modifier.size(28.dp))
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
                // Portfolio Summary Hero Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Sapphire.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(22.dp),
                    color = DashboardColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(Color(0xFF0F1E40), DashboardColors.surface)))
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("PORTFOLIO NET WORTH", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Sapphire, letterSpacing = 1.sp)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (totalGain >= 0) EmeraldDim.copy(alpha = 0.6f) else CrimsonDim.copy(alpha = 0.6f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (totalGain >= 0) Emerald.copy(alpha = 0.4f) else Crimson.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "${if (totalGain >= 0) "+" else ""}${String.format("%.1f", returnPercent)}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (totalGain >= 0) Emerald else Crimson,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = totalCurrent.toINR(),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DashboardColors.textPrimary
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("INVESTED CAPITAL", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                    Text(totalInvested.toINR(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textSecondary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("OVERALL RETURN", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                    Text(
                                        text = "${if (totalGain >= 0) "+" else ""}${totalGain.toINR()}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (totalGain >= 0) Emerald else Crimson
                                    )
                                }
                            }
                        }
                    }
                }

                if (investments.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Sapphire.copy(alpha = 0.15f))
                                    .border(1.dp, Sapphire.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ShowChart, null, Modifier.size(36.dp), tint = Sapphire)
                            }
                            Text("No investments tracked yet", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                            Text("Track SIPs, Mutual Funds, Stocks and Gold in one place", fontSize = 13.sp, color = DashboardColors.textSecondary)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(investments, key = { it.id }) { inv ->
                            val gain = inv.currentValue - inv.investedAmount
                            val gainPercent = if (inv.investedAmount > 0) (gain / inv.investedAmount) * 100 else 0.0

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                color = DashboardColors.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.surface)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Sapphire.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(getAssetEmoji(inv.type), fontSize = 18.sp)
                                            }
                                            Column {
                                                Text(inv.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                                                Text(inv.type, fontSize = 11.sp, color = Sapphire, fontWeight = FontWeight.SemiBold)
                                            }
                                        }

                                        IconButton(
                                            onClick = { scope.launch { repo.deleteInvestment(inv) } },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, "Delete", tint = Crimson.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Spacer(Modifier.height(14.dp))
                                    HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Column {
                                            Text("INVESTED", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                            Text(inv.investedAmount.toINR(), fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DashboardColors.textSecondary)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("CURRENT VALUE", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text(inv.currentValue.toINR(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = DashboardColors.textPrimary)
                                                Text(
                                                    text = "(${if (gain >= 0) "+" else ""}${String.format("%.1f", gainPercent)}%)",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (gain >= 0) Emerald else Crimson
                                                )
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
    }

    if (showDialog) {
        AddInvestmentDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, type, invested, current ->
                scope.launch {
                    repo.addInvestment(
                        Investment(
                            name = name,
                            type = type,
                            investedAmount = invested,
                            currentValue = current,
                            startDate = System.currentTimeMillis()
                        )
                    )
                    showDialog = false
                }
            }
        )
    }
}

@Composable
fun AddInvestmentDialog(onDismiss: () -> Unit, onAdd: (String, String, Double, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(InvestmentTypes[0]) }
    var investedAmount by remember { mutableStateOf("") }
    var currentValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DashboardColors.surface,
        shape = RoundedCornerShape(22.dp),
        title = { Text("Add Investment Asset", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Asset Name (e.g. Nifty 50 Index Fund)", color = DashboardColors.textSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Sapphire,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Sapphire,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Asset Type", fontSize = 12.sp, color = DashboardColors.textSecondary, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InvestmentTypes.take(4).forEach { t ->
                        val isSel = selectedType == t
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedType = t },
                            color = if (isSel) Sapphire else DashboardColors.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Sapphire else DashboardColors.border)
                        ) {
                            Text(
                                text = t,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else DashboardColors.textSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = investedAmount,
                    onValueChange = { investedAmount = it },
                    label = { Text("Invested Amount (₹)", color = DashboardColors.textSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Sapphire,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Sapphire,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currentValue,
                    onValueChange = { currentValue = it },
                    label = { Text("Current Valuation (₹)", color = DashboardColors.textSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Sapphire,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Sapphire,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val inv = investedAmount.toDoubleOrNull() ?: return@Button
                    val cur = currentValue.toDoubleOrNull() ?: inv
                    onAdd(name.ifBlank { "Investment Asset" }, selectedType, inv, cur)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Sapphire),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Save Asset", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = DashboardColors.textSecondary) }
        }
    )
}

private fun getAssetEmoji(type: String): String {
    val t = type.lowercase()
    return when {
        t.contains("sip") || t.contains("mutual") -> "📈"
        t.contains("stock") || t.contains("equity") -> "📊"
        t.contains("gold") -> "🥇"
        t.contains("fd") || t.contains("deposit") -> "🏦"
        t.contains("ppf") || t.contains("provident") -> "🛡️"
        t.contains("crypto") -> "🪙"
        else -> "💎"
    }
}
