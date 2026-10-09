package com.example.financeapp.ui.dashboard

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.financeapp.ui.theme.bounceClick
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financeapp.data.Card
import com.example.financeapp.ui.theme.Crimson
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Ink100
import com.example.financeapp.ui.theme.Ink300
import com.example.financeapp.util.toINR
import androidx.compose.ui.tooling.preview.Preview
import com.example.financeapp.ui.theme.FinanceAppTheme

@Composable
fun rememberDeviceTilt(): Offset {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var tiltOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    DisposableEffect(sensorManager) {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    tiltOffset = Offset(orientation[2] * 300f, orientation[1] * -300f)
                } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    tiltOffset = Offset(event.values[0] * -30f, event.values[1] * 30f)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { sensorManager.unregisterListener(listener) }
    }
    return tiltOffset
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
        viewModel: DashboardViewModel,
        onNavigateToIncome: () -> Unit,
        onNavigateToExpense: () -> Unit,
        onNavigateToEmi: () -> Unit,
        onNavigateToInvestment: () -> Unit,
        onNavigateToCalculator: () -> Unit,
        onNavigateToLoanCompare: () -> Unit,
        onNavigateToImportStatement: () -> Unit,
        onNavigateToAddBankAccount: () -> Unit = {},
        onNavigateToHistory: () -> Unit = {},
        onEditCard: (Card) -> Unit = {}
) {
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val balance by viewModel.balance.collectAsStateWithLifecycle()
    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()

    val animatedBalance by
            animateFloatAsState(
                    targetValue = balance.toFloat(),
                    animationSpec = tween(1000, easing = FastOutSlowInEasing),
                    label = "animatedBalance"
            )

    var isVisible by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(Unit) { isVisible = true }

    val alpha1 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 100, FastOutSlowInEasing), label = "alpha1")
    val offsetY1 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 100, FastOutSlowInEasing), label = "y1")

    val alpha2 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 200, FastOutSlowInEasing), label = "alpha2")
    val offsetY2 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 200, FastOutSlowInEasing), label = "y2")

    val alpha3 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 300, FastOutSlowInEasing), label = "alpha3")
    val offsetY3 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 300, FastOutSlowInEasing), label = "y3")

    val alpha4 by animateFloatAsState(if (isVisible) 1f else 0f, tween(800, 400, FastOutSlowInEasing), label = "alpha4")
    val offsetY4 by animateFloatAsState(if (isVisible) 0f else 60f, tween(800, 400, FastOutSlowInEasing), label = "y4")

    Box(
            modifier =
                    Modifier.fillMaxSize()
                            .background(DashboardColors.bg()) 
    ) {


        // ─── Scrollable Content ─────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp)
        ) {
            // ─── 1. Header Bar ──────────────────────────────────────────
            Row(
                    modifier =
                            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 14.dp)
                                    .graphicsLayer { alpha = alpha1; translationY = offsetY1 },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        text = "Dashboard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DashboardColors.textPrimary(),
                        letterSpacing = (-0.5).sp
                )

                // Notification Bell Pill
                Surface(
                        shape = CircleShape,
                        color = DashboardColors.surface(),
                        border = BorderStroke(1.dp, DashboardColors.border()),
                        modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                                Icons.Filled.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = DashboardColors.textPrimary(),
                                modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ─── 2. Gradient Hero Balance Card with Floating Glass Pill ─
            Box(modifier = Modifier.graphicsLayer { alpha = alpha1; translationY = offsetY1 }) {
                HeroGradientBalanceCard(
                        balance = animatedBalance.toDouble(),
                        totalIncome = totalIncome,
                        totalExpense = totalExpense
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ─── 3. Metall Cards ────────────────────────────────
            Box(modifier = Modifier.graphicsLayer { alpha = alpha2; translationY = offsetY2 }) {
                MetallCardSection(
                        cards = cards,
                        onAddAccount = onNavigateToAddBankAccount,
                        onEditCard = onEditCard
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ─── 4. Quick Actions ───────────────────────────────────────
            Box(modifier = Modifier.graphicsLayer { alpha = alpha3; translationY = offsetY3 }) {
                QuickActionsSection(
                        onCalculator = onNavigateToCalculator,
                        onIncome = onNavigateToIncome,
                        onExpense = onNavigateToExpense,
                        onEmi = onNavigateToEmi,
                        onInvest = onNavigateToInvestment,
                        onCompare = onNavigateToLoanCompare,
                        onImport = onNavigateToImportStatement
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ─── 5. Recent Transactions ─────────────────────────────────
            Box(modifier = Modifier.graphicsLayer { alpha = alpha4; translationY = offsetY4 }) {
                RecentTransactionsSection(
                        transactions = recentTransactions,
                        onSeeAll = onNavigateToHistory
                )
            }
        }
    }
}

// ─── Helper Composables Below ──────────────────────────────────────

// ─── Component 2: Gradient Hero Balance Card ─────────────────────────
@Composable
private fun HeroGradientBalanceCard(balance: Double, totalIncome: Double, totalExpense: Double) {
    Surface(
            modifier =
                    Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(26.dp),
            color = Color.Transparent,
            border =
                    BorderStroke(
                            1.dp,
                            Color(0xFF3B82F6).copy(alpha = 0.4f)
                    )
    ) {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(26.dp))
                                .background(
                                        Brush.linearGradient(
                                                colors =
                                                        listOf(
                                                                Color(0xFF3B82F6).copy(alpha = 0.35f),
                                                                Color(0xFF1E40AF).copy(alpha = 0.15f)
                                                        )
                                        )
                                )
                                .background(
                                        Brush.radialGradient(
                                                colors = listOf(Color.White.copy(alpha = 0.1f), Color.Transparent),
                                                radius = 600f
                                        )
                                )
                                .padding(top = 26.dp, start = 18.dp, end = 18.dp, bottom = 18.dp),
                contentAlignment = Alignment.Center
        ) {
            Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                        text = "Total balance",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFBFDBFE).copy(alpha = 0.85f),
                        letterSpacing = 0.4.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                val displayBalance = if (balance > 0) balance.toINR() else "₹ 0"
                Text(
                        text = displayBalance,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Floating Glass Pill showing Monthly Income vs Expenses
                Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = DashboardColors.glassBg(),
                        border =
                                BorderStroke(
                                        1.dp,
                                        DashboardColors.glassBorder()
                                )
                ) {
                    Row(
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Monthly Income
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                    text = "Monthly Income",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFE2E8F0).copy(alpha = 0.75f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Green bar
                            Box(
                                    modifier =
                                            Modifier.fillMaxWidth(0.92f)
                                                    .height(2.5.dp)
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(Color(0xFF10B981))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                        modifier =
                                                Modifier.size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                val incText = totalIncome.toINR()
                                Text(
                                        text = incText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DashboardColors.textPrimary()
                                )
                            }
                        }

                        // Divider line
                        Box(
                                modifier =
                                        Modifier.width(1.dp)
                                                .height(38.dp)
                                                .background(DashboardColors.glassBorder())
                        )

                        // Expenses
                        Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                            Text(
                                    text = "Expenses",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFE2E8F0).copy(alpha = 0.75f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Coral bar
                            Box(
                                    modifier =
                                            Modifier.fillMaxWidth(0.92f)
                                                    .height(2.5.dp)
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(Color(0xFFEF4444))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                        modifier =
                                                Modifier.size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFFEF4444))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                val expText = totalExpense.toINR()
                                Text(
                                        text = expText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DashboardColors.textPrimary()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Component 3: Metall Card Section ───────────────────────
@Composable
private fun MetallCardSection(cards: List<Card>, onAddAccount: () -> Unit, onEditCard: (Card) -> Unit = {}) {
    Column {
        // Section Header
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                    text = "Your Cards",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.textPrimary()
            )

            Text(
                    text = "Scroll",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = DashboardColors.textSecondary()
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Scrollable Cards Row
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (cards.isEmpty()) {
                Surface(
                    modifier = Modifier.width(200.dp).height(124.dp).bounceClick { onAddAccount() },
                    shape = RoundedCornerShape(18.dp),
                    color = DashboardColors.surface(),
                    border = BorderStroke(1.dp, DashboardColors.border())
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = DashboardColors.textPrimary())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Add Card", color = DashboardColors.textPrimary(), fontSize = 14.sp)
                    }
                }
            } else {
                cards.forEach { card ->
                    MetallicCreditCard(
                            holderName = card.cardHolderName.ifBlank { "User" },
                            last4 =
                                    if (card.cardNumber.length >= 4) card.cardNumber.takeLast(4)
                                    else "****",
                            isTitanium = card.cardNetwork.hashCode() % 2 == 0,
                            onClick = { onEditCard(card) }
                    )
                }
            }
        }
    }
}

// ─── Metallic Brushed Card Item ──────────────────────────────────────
@Composable
private fun MetallicCreditCard(holderName: String, last4: String, isTitanium: Boolean, onClick: () -> Unit = {}) {
    val tilt = rememberDeviceTilt()
    
    val metallicBrush =
            if (isTitanium) {
                Brush.linearGradient(
                        colors =
                                listOf(
                                        Color(0xFF7E828E),
                                        Color(0xFFD4D8E2),
                                        Color(0xFF676A76),
                                        Color(0xFFA1A5B2)
                                ),
                        start = Offset(0f, 0f) + tilt,
                        end = Offset(1000f, 1000f) + tilt
                )
            } else {
                Brush.linearGradient(
                        colors =
                                listOf(
                                        Color(0xFF64748B),
                                        Color(0xFFCBD5E1),
                                        Color(0xFF475569),
                                        Color(0xFF94A3B8)
                                ),
                        start = Offset(0f, 0f) + tilt,
                        end = Offset(1000f, 1000f) + tilt
                )
            }

    Surface(
            modifier =
                    Modifier.width(200.dp)
                            .height(124.dp)
                            .bounceClick { onClick() },
            shape = RoundedCornerShape(18.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
    ) {
        Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)).background(metallicBrush).padding(14.dp)) {
            Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row: EMV Chip & Contactless Waves
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    // Golden EMV Chip
                    MetallicChip()

                    // Contactless NFC Wave
                    Icon(
                            Icons.Filled.Contactless,
                            contentDescription = "Contactless",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(18.dp)
                    )
                }

                // Middle: Embossed Card Dots
                Text(
                        text = "•••• •••• •••• $last4",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                )

                // Bottom row: Holder Name & Overlapping Mastercard Circles
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = holderName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.85f)
                    )

                    // Mastercard Logo (Two overlapping translucent circles)
                    Row {
                        Box(
                                modifier =
                                        Modifier.size(16.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.38f))
                        )
                        Box(
                                modifier =
                                        Modifier.offset(x = (-6).dp)
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.22f))
                        )
                    }
                }
            }
        }
    }
}

// ─── Platinum Metallic Chip ───────────────────────────────────────────
@Composable
private fun MetallicChip() {
    Canvas(modifier = Modifier.size(width = 24.dp, height = 18.dp)) {
        val w = size.width
        val h = size.height

        drawRoundRect(
                brush =
                        Brush.linearGradient(
                                colors =
                                        listOf(
                                                Color(0xFFE2E8F0),
                                                Color(0xFFCBD5E1),
                                                Color(0xFF94A3B8)
                                        )
                        ),
                size = Size(w, h),
                cornerRadius = CornerRadius(4.dp.toPx())
        )

        // Chip etching lines
        val lineCol = Color(0xFF64748B).copy(alpha = 0.65f)
        val strokeW = 0.8.dp.toPx()
        drawLine(lineCol, Offset(w * 0.4f, 0f), Offset(w * 0.4f, h), strokeWidth = strokeW)
        drawLine(lineCol, Offset(w * 0.65f, 0f), Offset(w * 0.65f, h), strokeWidth = strokeW)
        drawLine(lineCol, Offset(0f, h * 0.5f), Offset(w, h * 0.5f), strokeWidth = strokeW)
    }
}

// ─── Component 4: Quick Actions Section (6 Glass Tiles) ───────────────
@Composable
private fun QuickActionsSection(
        onCalculator: () -> Unit,
        onIncome: () -> Unit,
        onExpense: () -> Unit,
        onEmi: () -> Unit,
        onInvest: () -> Unit,
        onCompare: () -> Unit,
        onImport: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.textPrimary()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Glassmorphic Dark Container
        Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = DashboardColors.surface(),
                border = BorderStroke(1.dp, DashboardColors.border())
        ) {
            Row(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 14.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                QuickActionTile(
                        label = "Calculator",
                        icon = Icons.Filled.Calculate,
                        tileColor = Color(0xFF26123D),
                        accentColor = Color(0xFF60A5FA),
                        onClick = onCalculator
                )
                QuickActionTile(
                        label = "Income+",
                        icon = Icons.Filled.Add,
                        tileColor = Color(0xFF072D1E),
                        accentColor = Color(0xFF34D399),
                        onClick = onIncome
                )
                QuickActionTile(
                        label = "Expense-",
                        icon = Icons.Filled.Remove,
                        tileColor = Color(0xFF361016),
                        accentColor = Color(0xFFF87171),
                        onClick = onExpense
                )
                QuickActionTile(
                        label = "EMI",
                        icon = Icons.Filled.CreditCard,
                        tileColor = Color(0xFF0C2445),
                        accentColor = Color(0xFF60A5FA),
                        onClick = onEmi
                )
                QuickActionTile(
                        label = "Invest",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        tileColor = Color(0xFF342306),
                        accentColor = Color(0xFFFBBF24),
                        onClick = onInvest
                )
                QuickActionTile(
                        label = "Compare",
                        icon = Icons.AutoMirrored.Filled.CompareArrows,
                        tileColor = Color(0xFF072E2E),
                        accentColor = Color(0xFF2DD4BF),
                        onClick = onCompare
                )
                QuickActionTile(
                        label = "Import",
                        icon = Icons.Filled.UploadFile,
                        tileColor = Color(0xFF2E0C23),
                        accentColor = Color(0xFFF472B6),
                        onClick = onImport
                )
            }
        }
    }
}

@Composable
private fun QuickActionTile(
        label: String,
        icon: ImageVector,
        tileColor: Color,
        accentColor: Color,
        onClick: () -> Unit
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        // Glassmorphism wrapper
        Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.Transparent,
                border =
                        BorderStroke(
                                1.dp,
                                accentColor.copy(alpha = 0.4f)
                        ),
                modifier = Modifier.size(48.dp).bounceClick { onClick() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(tileColor.copy(alpha = 0.2f))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.White.copy(alpha = 0.02f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                color = DashboardColors.textPrimary(),
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

// ─── Component 5: Recent Transactions Section ────────────────────────
@Composable
private fun RecentTransactionsSection(transactions: List<RecentTransaction>, onSeeAll: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                    text = "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DashboardColors.textPrimary()
            )

            Text(
                    text = "See all",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF60A5FA),
                    modifier = Modifier.clickable(onClick = onSeeAll)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        val displayList = transactions.take(5)

        if (displayList.isEmpty()) {
            Text(
                text = "No recent transactions. Tap + to add.",
                color = DashboardColors.textSecondary(),
                modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            val dotColors =
                    listOf(
                            Color(0xFFA855F7), // Purple
                            Color(0xFFEF4444), // Coral/Red
                            Color(0xFFF59E0B), // Yellow/Amber
                            Color(0xFF10B981), // Green
                            Color(0xFF3B82F6) // Blue
                    )

        displayList.forEachIndexed { index, tx ->
            val dotColor = dotColors[index % dotColors.size]
            Row(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .bounceClick { onSeeAll() }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category Colored Dot
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(dotColor))

                    Text(
                            text = tx.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DashboardColors.textPrimary()
                    )
                }

                val prefix = if (tx.isIncome) "" else "- "
                Text(
                        text = "$prefix₹ ${String.format("%,.0f", tx.amount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DashboardColors.textPrimary()
                )
            }

            if (index < displayList.lastIndex) {
                HorizontalDivider(color = DashboardColors.border(), thickness = 1.dp)
            }
        }
        }
    }
}

// ─── Legacy compatibility composables ─────────────────

@Composable
fun TransactionItem(
        title: String,
        subtitle: String,
        amount: Double,
        isIncome: Boolean,
        account: String
) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                    modifier =
                            Modifier.size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isIncome) Emerald else Crimson)
            )
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Ink100)
                Text(subtitle, fontSize = 11.sp, color = Ink300)
            }
        }
        Text(
                text = "${if (isIncome) "+" else "-"}₹${String.format("%,.0f", amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) Emerald else Ink100
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "TransactionItem Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun TransactionItemLightPreview() {
    FinanceAppTheme {
        TransactionItem(
            title = "Salary",
            subtitle = "Oct 10, 2026",
            amount = 75000.0,
            isIncome = true,
            account = "HDFC Bank"
        )
    }
}

@Preview(name = "TransactionItem Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TransactionItemDarkPreview() {
    FinanceAppTheme {
        TransactionItem(
            title = "Rent",
            subtitle = "Oct 5, 2026",
            amount = 25000.0,
            isIncome = false,
            account = "SBI"
        )
    }
}
