package com.example.financeapp.ui.income

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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import com.example.financeapp.data.Income
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors
import com.example.financeapp.util.IncomeSources
import com.example.financeapp.util.toINR
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(repo: FinanceRepository, onBack: () -> Unit) {
    val incomes by repo.getAllIncomes().collectAsStateWithLifecycle(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val totalIncome = remember(incomes) { incomes.sumOf { it.amount } }

    Scaffold(
        containerColor = DashboardColors.bg,
        topBar = {
            TopAppBar(
                title = { Text("Income Streams", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
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
                containerColor = Emerald,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.shadow(16.dp, CircleShape, spotColor = Emerald)
            ) {
                Icon(Icons.Default.Add, "Add Income", modifier = Modifier.size(28.dp))
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
                // Total Income Header Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(10.dp, RoundedCornerShape(22.dp), spotColor = Emerald.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(22.dp),
                    color = DashboardColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(EmeraldDim.copy(alpha = 0.4f), DashboardColors.surface)))
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("TOTAL RECORDED INFLOW", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Emerald, letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = totalIncome.toINR(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Emerald
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${incomes.size} active source(s)", fontSize = 11.sp, color = DashboardColors.textSecondary)
                            }

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldDim)
                                    .border(1.dp, Emerald.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Emerald, modifier = Modifier.size(26.dp))
                            }
                        }
                    }
                }

                if (incomes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldDim.copy(alpha = 0.4f))
                                    .border(1.dp, Emerald.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, null, Modifier.size(36.dp), tint = Emerald)
                            }
                            Text("No income recorded yet", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                            Text("Tap the + button to add your salary or earnings", fontSize = 13.sp, color = DashboardColors.textSecondary)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(incomes, key = { it.id }) { income ->
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
                                                .background(EmeraldDim.copy(alpha = 0.5f))
                                                .border(1.dp, Emerald.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("💰", fontSize = 18.sp)
                                        }
                                        Column {
                                            Text(income.source, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                                            if (income.description.isNotEmpty()) {
                                                Text(income.description, fontSize = 12.sp, color = DashboardColors.textSecondary)
                                            }
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = "+${income.amount.toINR()}",
                                            color = Emerald,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        IconButton(
                                            onClick = { scope.launch { repo.deleteIncome(income) } },
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
        AddIncomeDialog(
            onDismiss = { showDialog = false },
            onAdd = { amount, source, desc ->
                scope.launch {
                    repo.addIncome(Income(amount = amount, source = source, description = desc))
                    showDialog = false
                }
            }
        )
    }
}

@Composable
fun AddIncomeDialog(onDismiss: () -> Unit, onAdd: (Double, String, String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var selectedSource by remember { mutableStateOf(IncomeSources[0]) }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DashboardColors.surface,
        shape = RoundedCornerShape(22.dp),
        title = { Text("Add Income", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)", color = DashboardColors.textSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Emerald,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Emerald,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Source Category", fontSize = 12.sp, color = DashboardColors.textSecondary, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IncomeSources.take(3).forEach { src ->
                        val isSel = selectedSource == src
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedSource = src },
                            color = if (isSel) Emerald else DashboardColors.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Emerald else DashboardColors.border)
                        ) {
                            Text(
                                text = src,
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
                    label = { Text("Notes / Description", color = DashboardColors.textSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Emerald,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Emerald,
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
                    onAdd(a, selectedSource, description)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Save Income", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = DashboardColors.textSecondary) }
        }
    )
}
