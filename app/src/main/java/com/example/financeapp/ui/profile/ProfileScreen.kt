package com.example.financeapp.ui.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onSignOut: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isSyncing by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    // Sync rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "SyncRotate")
    val syncRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SyncRotation"
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
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            // Header Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Account & Settings",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DashboardColors.textPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Preferences, Security & Cloud Sync",
                        fontSize = 12.sp,
                        color = DashboardColors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DashboardColors.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
                ) {
                    IconButton(
                        onClick = {
                            if (!isSyncing) {
                                isSyncing = true
                                coroutineScope.launch {
                                    delay(1200)
                                    isSyncing = false
                                    Toast.makeText(context, "Cloud sync successful! All databases up-to-date.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            Icons.Filled.Sync,
                            contentDescription = "Sync",
                            tint = if (isSyncing) Violet400 else Emerald,
                            modifier = Modifier
                                .size(18.dp)
                                .then(if (isSyncing) Modifier.rotate(syncRotation) else Modifier)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // User Profile Hero Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Violet500.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(22.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Violet900.copy(alpha = 0.35f), DashboardColors.surface)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar with glowing gradient border
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Violet600, Sapphire)))
                                .border(2.dp, Violet300.copy(alpha = 0.7f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "KF",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Kiran Finance",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DashboardColors.textPrimary
                                )
                                Icon(Icons.Filled.Verified, contentDescription = "Verified", tint = Sapphire, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = "kiran@example.com",
                                fontSize = 12.sp,
                                color = DashboardColors.textSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            // Cloud Sync badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Emerald)
                                )
                                Text("Cloud Sync: Active", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Emerald)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Financial Health Score Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("FINANCIAL HEALTH", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textSecondary, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("840 / 900", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Emerald)
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = EmeraldDim.copy(alpha = 0.4f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Emerald.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "EXCELLENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(DashboardColors.surface)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.93f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(3.dp))
                                .background(Brush.horizontalGradient(listOf(Emerald, Violet400)))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HealthMetricMini("Savings Rate", "34%", Emerald)
                        HealthMetricMini("Budget Health", "92%", Violet400)
                        HealthMetricMini("Debt Load", "Low", Sapphire)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Group: Security & App
            Text(
                text = "SECURITY & PREFERENCES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.textSecondary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 6.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    SettingsSwitchRow(
                        icon = Icons.Filled.Fingerprint,
                        title = "Biometric App Lock",
                        subtitle = "Fingerprint / Face ID for vault access",
                        checked = biometricEnabled,
                        onCheckedChange = { biometricEnabled = it }
                    )
                    HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                    SettingsSwitchRow(
                        icon = Icons.Filled.NotificationsActive,
                        title = "Smart Budget Alerts",
                        subtitle = "Get notified before exceeding category limit",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                    HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                    SettingsNavRow(
                        icon = Icons.Filled.CurrencyRupee,
                        title = "Default Currency",
                        value = "INR (₹)",
                        onClick = { Toast.makeText(context, "Default currency is Indian Rupee (₹)", Toast.LENGTH_SHORT).show() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Group: Data & Storage
            Text(
                text = "DATA & SYNC",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DashboardColors.textSecondary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 6.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = DashboardColors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DashboardColors.border)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    SettingsNavRow(
                        icon = Icons.Filled.CloudUpload,
                        title = "Cloud Sync & Backup",
                        value = "Firestore Active",
                        onClick = {
                            if (!isSyncing) {
                                isSyncing = true
                                coroutineScope.launch {
                                    delay(1000)
                                    isSyncing = false
                                    Toast.makeText(context, "Full cloud backup complete!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                    HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                    SettingsNavRow(
                        icon = Icons.Filled.Download,
                        title = "Export Data (JSON/CSV)",
                        value = "",
                        onClick = { Toast.makeText(context, "Exporting personal finance data...", Toast.LENGTH_SHORT).show() }
                    )
                    HorizontalDivider(color = DashboardColors.surface, thickness = 1.dp)
                    SettingsNavRow(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        title = "Help & Support",
                        value = "",
                        onClick = { Toast.makeText(context, "Support desk: support@financeapp.internal", Toast.LENGTH_SHORT).show() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        if (!isSyncing) {
                            isSyncing = true
                            coroutineScope.launch {
                                delay(1200)
                                isSyncing = false
                                Toast.makeText(context, "Database synced with Cloud Firestore!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Violet600),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Sync,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .then(if (isSyncing) Modifier.rotate(syncRotation) else Modifier)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isSyncing) "Syncing with Cloud..." else "Sync Now", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                OutlinedButton(
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Crimson),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Crimson.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Version info footer
            Text(
                text = "FinanceApp v2.4.0 • Encrypted Vault Engine",
                fontSize = 11.sp,
                color = DashboardColors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Sign out confirmation dialog
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            containerColor = DashboardColors.surface,
            titleContentColor = DashboardColors.textPrimary,
            textContentColor = DashboardColors.textSecondary,
            title = { Text("Sign Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to sign out? Your offline database will remain safely saved on this device.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutDialog = false
                        onSignOut()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Crimson)
                ) {
                    Text("Sign Out", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel", color = DashboardColors.textSecondary)
                }
            }
        )
    }
}

@Composable
private fun HealthMetricMini(label: String, value: String, color: Color) {
    Column {
        Text(label, fontSize = 10.sp, color = DashboardColors.textSecondary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DashboardColors.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Violet400, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textPrimary)
                Text(subtitle, fontSize = 11.sp, color = DashboardColors.textSecondary)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Violet600,
                uncheckedThumbColor = DashboardColors.textSecondary,
                uncheckedTrackColor = DashboardColors.surface
            )
        )
    }
}

@Composable
private fun SettingsNavRow(
    icon: ImageVector,
    title: String,
    value: String = "",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DashboardColors.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Sapphire, modifier = Modifier.size(20.dp))
            }
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DashboardColors.textPrimary)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (value.isNotBlank()) {
                Text(value, fontSize = 12.sp, color = DashboardColors.textSecondary, fontWeight = FontWeight.Medium)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = DashboardColors.textSecondary, modifier = Modifier.size(18.dp))
        }
    }
}
