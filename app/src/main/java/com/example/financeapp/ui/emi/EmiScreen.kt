package com.example.financeapp.ui.emi

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financeapp.calculator.EmiCalculator
import com.example.financeapp.data.Emi
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors
import com.example.financeapp.util.toINR
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmiScreen(repo: FinanceRepository, onBack: () -> Unit) {
    val emis by repo.getAllEmis().collectAsStateWithLifecycle(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val totalMonthlyEmi = remember(emis) { emis.sumOf { it.emiAmount } }
    val totalPrincipal = remember(emis) { emis.sumOf { it.principal } }

    Scaffold(
        containerColor = DashboardColors.bg,
        topBar = {
            TopAppBar(
                title = { Text("Loans & EMI Portfolio", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
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
                containerColor = Amber,
                contentColor = DashboardColors.bg,
                shape = CircleShape,
                modifier = Modifier.shadow(16.dp, CircleShape, spotColor = Amber)
            ) {
                Icon(Icons.Default.Add, "Add EMI", modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DashboardColors.bg)
                .padding(padding)
        ) {
            // Ambient amber glow

Column(modifier = Modifier.fillMaxSize()) {
                // Header Obligation Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(10.dp, RoundedCornerShape(22.dp), spotColor = Amber.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(22.dp),
                    color = DashboardColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(Amber.copy(alpha = 0.2f), DashboardColors.surface)))
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("MONTHLY EMI OBLIGATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Amber, letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = totalMonthlyEmi.toINR(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Amber
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Total Loan Balance: ${totalPrincipal.toINR()}", fontSize = 11.sp, color = DashboardColors.textSecondary)
                            }

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Amber.copy(alpha = 0.2f))
                                    .border(1.dp, Amber.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Amber, modifier = Modifier.size(26.dp))
                            }
                        }
                    }
                }

                if (emis.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Amber.copy(alpha = 0.15f))
                                    .border(1.dp, Amber.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalance, null, Modifier.size(36.dp), tint = Amber)
                            }
                            Text("No active loans or EMIs", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                            Text("Tap the + button to add a home, car or personal loan", fontSize = 13.sp, color = DashboardColors.textSecondary)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(emis, key = { it.id }) { emi ->
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
                                                    .background(Amber.copy(alpha = 0.18f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.RealEstateAgent, contentDescription = null, tint = Amber, modifier = Modifier.size(20.dp))
                                            }
                                            Text(emi.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary)
                                        }

                                        IconButton(
                                            onClick = { scope.launch { repo.deleteEmi(emi) } },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, "Delete", tint = Crimson.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))
                                    HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                                    Spacer(Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("PRINCIPAL", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                            Text(emi.principal.toINR(), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary)
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("RATE", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                            Text("${emi.interestRate}% p.a.", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("TENURE", fontSize = 9.sp, color = DashboardColors.textSecondary, letterSpacing = 0.8.sp)
                                            Text("${emi.termMonths} mos", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary)
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = DashboardColors.surface,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Monthly EMI Payment", fontSize = 12.sp, color = DashboardColors.textSecondary, fontWeight = FontWeight.Medium)
                                            Text(
                                                text = emi.emiAmount.toINR(),
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Amber
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

    if (showDialog) {
        AddEmiDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, principal, rate, tenure, emiAmount ->
                scope.launch {
                    repo.addEmi(
                        Emi(
                            name = name,
                            principal = principal,
                            interestRate = rate,
                            termMonths = tenure,
                            emiAmount = emiAmount,
                            startDate = System.currentTimeMillis(),
                            nextDueDate = System.currentTimeMillis() + 30L * 24 * 3600 * 1000
                        )
                    )
                    showDialog = false
                }
            }
        )
    }
}

@Composable
fun AddEmiDialog(onDismiss: () -> Unit, onAdd: (String, Double, Double, Int, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var tenure by remember { mutableStateOf("") }

    val calculatedEmi = remember(principal, rate, tenure) {
        val p = principal.toDoubleOrNull() ?: 0.0
        val r = rate.toDoubleOrNull() ?: 0.0
        val t = tenure.toIntOrNull() ?: 0
        if (p > 0 && r > 0 && t > 0) {
            EmiCalculator.calculate(p, r, t).monthlyEmi
        } else 0.0
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DashboardColors.surface,
        shape = RoundedCornerShape(22.dp),
        title = { Text("Add Loan / EMI", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Loan Name (e.g. HDFC Home Loan)", color = DashboardColors.textSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Amber,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Amber,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = principal,
                    onValueChange = { principal = it },
                    label = { Text("Principal Amount (₹)", color = DashboardColors.textSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Amber,
                        unfocusedBorderColor = DashboardColors.border,
                        focusedTextColor = DashboardColors.textPrimary,
                        unfocusedTextColor = DashboardColors.textPrimary,
                        cursorColor = Amber,
                        focusedContainerColor = DashboardColors.surface,
                        unfocusedContainerColor = DashboardColors.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = rate,
                        onValueChange = { rate = it },
                        label = { Text("Rate (%)", color = DashboardColors.textSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Amber,
                            unfocusedBorderColor = DashboardColors.border,
                            focusedTextColor = DashboardColors.textPrimary,
                            unfocusedTextColor = DashboardColors.textPrimary,
                            cursorColor = Amber,
                            focusedContainerColor = DashboardColors.surface,
                            unfocusedContainerColor = DashboardColors.surface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = tenure,
                        onValueChange = { tenure = it },
                        label = { Text("Months", color = DashboardColors.textSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Amber,
                            unfocusedBorderColor = DashboardColors.border,
                            focusedTextColor = DashboardColors.textPrimary,
                            unfocusedTextColor = DashboardColors.textPrimary,
                            cursorColor = Amber,
                            focusedContainerColor = DashboardColors.surface,
                            unfocusedContainerColor = DashboardColors.surface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (calculatedEmi > 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Amber.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Amber.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Calculated EMI:", fontSize = 12.sp, color = Amber, fontWeight = FontWeight.Medium)
                            Text(calculatedEmi.toINR(), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Amber)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = principal.toDoubleOrNull() ?: return@Button
                    val r = rate.toDoubleOrNull() ?: return@Button
                    val t = tenure.toIntOrNull() ?: return@Button
                    val emi = if (calculatedEmi > 0) calculatedEmi else EmiCalculator.calculate(p, r, t).monthlyEmi
                    onAdd(name.ifBlank { "Personal Loan" }, p, r, t, emi)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Amber),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Save Loan", fontWeight = FontWeight.Bold, color = DashboardColors.bg) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = DashboardColors.textSecondary) }
        }
    )
}
