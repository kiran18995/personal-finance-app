package com.example.financeapp.ui.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.financeapp.ui.theme.bounceClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.financeapp.data.BankAccount
import com.example.financeapp.data.Card
import com.example.financeapp.ui.theme.*
import com.example.financeapp.ui.dashboard.DashboardColors

@Composable
fun CardsScreen(
    bankAccounts: List<BankAccount>,
    cards: List<Card>,
    onAddBankAccount: () -> Unit,
    onAddCard: () -> Unit,
    onEditBankAccount: (BankAccount) -> Unit = {},
    onEditCard: (Card) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Bank Accounts", "Credit Cards", "Debit Cards")

    val totalBankBalance = remember(bankAccounts) {
        bankAccounts.sumOf { it.currentBalance }
    }
    val totalCreditLimit = remember(cards) {
        cards.filter { it.cardType == "Credit" }.sumOf { it.creditLimit }
    }
    val totalOutstanding = remember(cards) {
        cards.filter { it.cardType == "Credit" }.sumOf { it.outstandingBalance }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DashboardColors.bg)
    ) {
        
Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Vault & Cards",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DashboardColors.textPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Manage linked accounts & virtual cards",
                        fontSize = 12.sp,
                        color = DashboardColors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onAddCard,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(DashboardColors.surface)
                            .border(1.dp, Violet500.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Filled.AddCard, contentDescription = "Add Card", tint = Violet400, modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = onAddBankAccount,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(DashboardColors.surface)
                            .border(1.dp, Emerald.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Filled.AccountBalance, contentDescription = "Add Bank", tint = Emerald, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Overview Summary Pill
            VaultOverviewPill(
                totalBankBalance = totalBankBalance,
                totalCreditLimit = totalCreditLimit,
                totalOutstanding = totalOutstanding,
                activeTab = selectedTab
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Segmented Tab Selector
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(22.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) Violet700 else Color.Transparent)
                                .clickable { selectedTab = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else DashboardColors.textSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content List
            Box(modifier = Modifier.weight(1f)) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                    },
                    label = "VaultTabAnimation"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> BankAccountsTab(bankAccounts, onAddBankAccount, onEditBankAccount)
                        1 -> CardsTab(cards.filter { it.cardType.equals("Credit", ignoreCase = true) }, "Credit", onAddCard, onEditCard)
                        2 -> CardsTab(cards.filter { !it.cardType.equals("Credit", ignoreCase = true) }, "Debit", onAddCard, onEditCard)
                    }
                }
            }
        }
    }
}

// ─── Vault Summary Pill ──────────────────────────────────────────────
@Composable
private fun VaultOverviewPill(
    totalBankBalance: Double,
    totalCreditLimit: Double,
    totalOutstanding: Double,
    activeTab: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = DashboardColors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (activeTab == 0) {
                Column {
                    Text("TOTAL LIQUID DEPOSITS", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("₹${String.format("%,.2f", totalBankBalance)}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Emerald)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldDim.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Emerald.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = Emerald, modifier = Modifier.size(14.dp))
                        Text("Active Vault", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald)
                    }
                }
            } else {
                Column {
                    Text("AVAILABLE CREDIT", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    val available = (totalCreditLimit - totalOutstanding).coerceAtLeast(0.0)
                    Text("₹${String.format("%,.0f", available)}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Violet300)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("TOTAL DUE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "₹${String.format("%,.0f", totalOutstanding)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (totalOutstanding > 0) Crimson else Ink200
                    )
                }
            }
        }
    }
}

// ─── Bank Accounts Tab ───────────────────────────────────────────────
@Composable
private fun BankAccountsTab(
    accounts: List<BankAccount>,
    onAddAccount: () -> Unit,
    onEditAccount: (BankAccount) -> Unit
) {
    if (accounts.isEmpty()) {
        EmptyVaultState(
            title = "No Bank Accounts Connected",
            subtitle = "Link your savings, salary or current accounts to track balances automatically.",
            buttonText = "Link Bank Account",
            icon = Icons.Filled.AccountBalance,
            accentColor = Emerald,
            onClick = onAddAccount
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(accounts) { account ->
                LuxuryBankAccountCard(account, onEditAccount)
            }
        }
    }
}

// ─── Cards Tab ───────────────────────────────────────────────────────
@Composable
private fun CardsTab(
    cards: List<Card>,
    cardCategory: String,
    onAddCard: () -> Unit,
    onEditCard: (Card) -> Unit
) {
    if (cards.isEmpty()) {
        EmptyVaultState(
            title = "No $cardCategory Cards Added",
            subtitle = "Store your $cardCategory card details securely with biometric-ready local encryption.",
            buttonText = "Add $cardCategory Card",
            icon = Icons.Filled.CreditCard,
            accentColor = if (cardCategory == "Credit") Violet400 else Sapphire,
            onClick = onAddCard
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cards) { card ->
                LuxuryPaymentCardItem(card, onEditCard)
            }
        }
    }
}

// ─── Luxury Bank Account Card ─────────────────────────────────────────
@Composable
private fun LuxuryBankAccountCard(account: BankAccount, onEditAccount: (BankAccount) -> Unit = {}) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val defaultSurface = DashboardColors.surface
    val defaultBorder = DashboardColors.border
    val bankTheme = remember(account.bankName, defaultSurface, defaultBorder) {
        val name = account.bankName.lowercase()
        when {
            name.contains("hdfc") -> listOf(Color(0xFF002E6E), Color(0xFF004C99))
            name.contains("sbi") || name.contains("state bank") -> listOf(Color(0xFF0B2E59), Color(0xFF1B5A99))
            name.contains("icici") -> listOf(Color(0xFF5B1015), Color(0xFF8B1E22))
            name.contains("axis") -> listOf(Color(0xFF500028), Color(0xFF8A0A4B))
            name.contains("kotak") -> listOf(Color(0xFF6E0D14), Color(0xFFA11722))
            name.contains("canara") -> listOf(Color(0xFF003E6B), Color(0xFF0A63A5))
            else -> listOf(defaultSurface, defaultBorder)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Violet500.copy(alpha = 0.15f))
            .bounceClick { onEditAccount(account) },
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.linearGradient(bankTheme))
                .padding(20.dp)
        ) {
            Column {
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
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(
                                text = account.bankName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (account.branchName.isNotBlank()) {
                                Text(
                                    text = account.branchName,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                    ) {
                        Text(
                            text = account.accountType.uppercase(),
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Account Number & Copy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val displayAcc = if (account.accountNumber.length > 4) {
                        "•••• •••• ${account.accountNumber.takeLast(4)}"
                    } else {
                        account.accountNumber.ifBlank { "•••• •••• 0000" }
                    }
                    Text(
                        text = displayAcc,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    if (account.accountNumber.isNotBlank()) {
                        IconButton(
                            onClick = {
                                clipboard.setText(AnnotatedString(account.accountNumber))
                                Toast.makeText(context, "Account number copied", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Balance and Holder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("CURRENT BALANCE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.6f), letterSpacing = 0.8.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹${String.format("%,.2f", account.currentBalance)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    if (account.ifscCode.isNotBlank()) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("IFSC CODE", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f), letterSpacing = 0.5.sp)
                            Text(
                                text = account.ifscCode,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Luxury Payment Card Item (EMV Chip, Contactless, Shimmer) ──────
@Composable
private fun LuxuryPaymentCardItem(card: Card, onEditCard: (Card) -> Unit = {}) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    var isNumberVisible by remember { mutableStateOf(false) }
    var isCardFrozen by remember { mutableStateOf(false) }

    // Palette based on Card Network & Type
    val cardGradient = remember(card.cardType, card.cardNetwork) {
        if (card.cardType.equals("Credit", ignoreCase = true)) {
            when (card.cardNetwork.lowercase()) {
                "visa" -> listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                "mastercard" -> listOf(Color(0xFF23074D), Color(0xFFCC5333))
                "rupay" -> listOf(Color(0xFF0B301A), Color(0xFF1E5E3A), Color(0xFF2E8B57))
                "american express", "amex" -> listOf(Color(0xFF141E30), Color(0xFF243B55))
                else -> listOf(Color(0xFF1A0B2E), Color(0xFF4C1D95), Color(0xFF6D28D9))
            }
        } else {
            when (card.cardNetwork.lowercase()) {
                "visa" -> listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFF415A77))
                "mastercard" -> listOf(Color(0xFF1C1917), Color(0xFF292524), Color(0xFF44403C))
                "rupay" -> listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF059669))
                else -> listOf(Color(0xFF111827), Color(0xFF1F2937), Color(0xFF374151))
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(22.dp), spotColor = Violet500.copy(alpha = 0.2f))
            .bounceClick { onEditCard(card) },
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isCardFrozen) Crimson.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.18f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.linearGradient(cardGradient))
                .padding(22.dp)
        ) {
            Column {
                // Top Row: Bank name / Card type & Network
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = card.bankName.ifBlank { "Antigravity Vault" },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${card.cardType.uppercase()} CARD",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                    }

                    // Network Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = card.cardNetwork.ifBlank { "CARD" }.uppercase(),
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // EMV Chip & Contactless Symbol
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Golden EMV Chip
                    EmvChipGraphic()

                    // Contactless NFC Wave Icon
                    Icon(
                        Icons.Filled.Contactless,
                        contentDescription = "Contactless",
                        tint = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Card Number display with hide/show
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val cardNumberText = if (isNumberVisible) {
                        "4111 8920 4452 ${card.cardNumber}"
                    } else {
                        "•••• •••• •••• ${card.cardNumber}"
                    }

                    Text(
                        text = cardNumberText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        letterSpacing = 3.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Row {
                        IconButton(
                            onClick = { isNumberVisible = !isNumberVisible },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                if (isNumberVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = "Toggle visibility",
                                tint = Color.White.copy(alpha = 0.75f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                clipboard.setText(AnnotatedString(card.cardNumber))
                                Toast.makeText(context, "Card digits copied", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.ContentCopy,
                                contentDescription = "Copy number",
                                tint = Color.White.copy(alpha = 0.75f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Holder name & Expiry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("CARD HOLDER", fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = card.cardHolderName.ifBlank { "AUTHORIZED HOLDER" }.uppercase(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("EXPIRES", fontSize = 9.sp, color = Color.White.copy(alpha = 0.5f), letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${String.format("%02d", card.expiryMonth)}/${card.expiryYear % 100}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                // Credit Card Specifics: Limit bar & actions
                if (card.cardType.equals("Credit", ignoreCase = true) && card.creditLimit > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    val ratio = (card.outstandingBalance / card.creditLimit).coerceIn(0.0, 1.0).toFloat()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Limit: ₹${String.format("%,.0f", card.creditLimit)}",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Due: ₹${String.format("%,.0f", card.outstandingBalance)} (${(ratio * 100).toInt()}%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ratio > 0.5f) Color(0xFFFF9E80) else Emerald
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(ratio)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (ratio > 0.5f) Crimson else Emerald)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Quick Actions row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardActionChip(
                        label = if (isCardFrozen) "Frozen" else "Freeze",
                        icon = if (isCardFrozen) Icons.Filled.Lock else Icons.Outlined.Lock,
                        tint = if (isCardFrozen) Crimson else Color.White,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            isCardFrozen = !isCardFrozen
                            Toast.makeText(context, if (isCardFrozen) "Card locked" else "Card unlocked", Toast.LENGTH_SHORT).show()
                        }
                    )
                    CardActionChip(
                        label = "Set Limit",
                        icon = Icons.Filled.Tune,
                        tint = Color.White,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Limit settings updated", Toast.LENGTH_SHORT).show()
                        }
                    )
                    CardActionChip(
                        label = "Pay Bill",
                        icon = Icons.Filled.Payments,
                        tint = Emerald,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Payment gateway opening...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CardActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .bounceClick(onClick = onClick),
        color = Color.White.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = tint)
        }
    }
}

// ─── Golden EMV Chip Graphic ──────────────────────────────────────────
@Composable
private fun EmvChipGraphic() {
    Canvas(modifier = Modifier.size(width = 38.dp, height = 28.dp)) {
        val w = size.width
        val h = size.height

        // Chip body
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFD54F), Color(0xFFFFB300), Color(0xFFFFA000))
            ),
            size = Size(w, h),
            cornerRadius = CornerRadius(6.dp.toPx())
        )

        // Chip micro-circuits
        val stroke = Stroke(width = 1.dp.toPx())
        val lineCol = Color(0xFF8D6E63).copy(alpha = 0.7f)

        // Center line
        drawLine(lineCol, Offset(w * 0.35f, 0f), Offset(w * 0.35f, h), strokeWidth = stroke.width)
        drawLine(lineCol, Offset(w * 0.65f, 0f), Offset(w * 0.65f, h), strokeWidth = stroke.width)
        drawLine(lineCol, Offset(0f, h * 0.5f), Offset(w, h * 0.5f), strokeWidth = stroke.width)
    }
}

// ─── Empty State ─────────────────────────────────────────────────────
@Composable
private fun EmptyVaultState(
    title: String,
    subtitle: String,
    buttonText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(36.dp))
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.textPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = DashboardColors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(46.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(buttonText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
