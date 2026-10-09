package com.example.financeapp.ui.transaction

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.data.BankAccount
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChooseAccountScreen(
    bankAccounts: List<BankAccount>,
    onDismiss: () -> Unit,
    onAccountSelected: (String) -> Unit,
    onNavigateToAddCard: () -> Unit = {}
) {
    var selectedAccount by remember {
        mutableStateOf(bankAccounts.firstOrNull()?.bankName ?: "Primary Account")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DashboardColors.bg()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(DashboardColors.surface())
                        .border(1.dp, DashboardColors.border(), CircleShape)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = DashboardColors.textPrimary())
                }

                Text(
                    text = "Choose Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { onAccountSelected(selectedAccount) },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Blue600)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Confirm", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "AVAILABLE ACCOUNTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.textSecondary(),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (bankAccounts.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = DashboardColors.surface(),
                    border = BorderStroke(1.dp, DashboardColors.border()),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = DashboardColors.textSecondary(), modifier = Modifier.size(36.dp))
                        Text("No Bank Accounts Connected", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary(), fontSize = 15.sp)
                        Text("Add an account to link your transactions.", color = DashboardColors.textSecondary(), fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(bankAccounts.size) { index ->
                        val account = bankAccounts[index]
                        val colors = listOf(Blue500, Sapphire, Emerald, Amber)
                        val color = colors[index % colors.size]

                        AccountItem(
                            id = account.bankName,
                            name = account.bankName,
                            holder = account.accountType,
                            balance = account.currentBalance,
                            color = color,
                            iconText = account.bankName.take(2).uppercase(),
                            isSelected = selectedAccount == account.bankName,
                            onClick = { selectedAccount = account.bankName }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Card Button
            Button(
                onClick = onNavigateToAddCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DashboardColors.surface()),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Blue500.copy(alpha = 0.4f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Blue400)
                    Text("Add new card or account", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary())
                }
            }
        }
    }
}

@Composable
fun AccountItem(
    id: String,
    name: String,
    holder: String,
    balance: Double,
    color: Color,
    iconText: String,
    isCash: Boolean = false,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) DashboardColors.surface() else DashboardColors.surface(),
        border = BorderStroke(1.dp, if (isSelected) Blue500 else DashboardColors.border())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCash) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DashboardColors.surface())
                        .border(1.dp, DashboardColors.border(), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Wallet, contentDescription = null, tint = Emerald)
                }
            } else {
                AccountIconBox(color = color, text = iconText)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary())
                Spacer(modifier = Modifier.height(2.dp))
                Text(holder, fontSize = 12.sp, color = DashboardColors.textSecondary())
            }

            Text(
                text = "₹${String.format("%,.2f", balance)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Blue400 else DashboardColors.textPrimary()
            )
        }
    }
}

@Composable
fun AccountIconBox(color: Color, text: String) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "ChooseAccountScreen Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "ChooseAccountScreen Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChooseAccountScreenPreview() {
    FinanceAppTheme {
        ChooseAccountScreen(
            bankAccounts = listOf(
                BankAccount(id = 1, bankName = "HDFC Bank", accountNumber = "•••• 4892", accountType = "Savings", currentBalance = 148500.0),
                BankAccount(id = 2, bankName = "ICICI Bank", accountNumber = "•••• 9102", accountType = "Salary", currentBalance = 75200.0)
            ),
            onDismiss = {},
            onAccountSelected = {}
        )
    }
}

