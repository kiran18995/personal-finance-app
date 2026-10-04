package com.example.financeapp.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financeapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) onLoginSuccess()
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink800)
    ) {
        // ─── Ambient glow orbs ────────────────────────────────────
        Box(
            modifier = Modifier
                .size(400.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .blur(140.dp)
                .background(Brush.radialGradient(listOf(Violet700.copy(0.5f), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 80.dp, y = 80.dp)
                .blur(120.dp)
                .background(Brush.radialGradient(listOf(Sapphire.copy(0.3f), Color.Transparent)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))

            // ─── Logo & Branding ──────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(700)) + slideInVertically(tween(700)) { -60 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Logo pill
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFF3B0764), Violet600)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("₹", fontSize = 44.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "My Finance",
                        style = MaterialTheme.typography.displaySmall,
                        color = Ink100,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Your complete financial companion",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink300,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(48.dp))

            // ─── Glass Form Card ──────────────────────────────────
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, 250)) + slideInVertically(tween(600, 250)) { 100 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Ink700)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tab Switcher: Sign In / Sign Up
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Ink800)
                            .padding(4.dp)
                    ) {
                        listOf("Sign In", "Create Account").forEachIndexed { i, label ->
                            val selected = (i == 1) == isSignUp
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selected) Violet700 else Color.Transparent)
                                    .clickableNoRipple { isSignUp = (i == 1) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selected) Color.White else Ink300,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Email field
                    PremiumTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email address",
                        leadingIcon = {
                            Icon(Icons.Filled.Email, null, tint = Violet400, modifier = Modifier.size(20.dp))
                        },
                        keyboardType = KeyboardType.Email
                    )

                    // Password field
                    PremiumTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, null, tint = Violet400, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    null,
                                    tint = Ink300,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password
                    )

                    // Primary Action Button
                    Button(
                        onClick = {
                            if (isSignUp) viewModel.signUpWithEmail(email, password)
                            else viewModel.signInWithEmail(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Violet600),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            if (isSignUp) "Create Account" else "Sign In",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // OR Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Ink500)
                        Text("or", style = MaterialTheme.typography.labelMedium, color = Ink300)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Ink500)
                    }

                    // Google Sign-In
                    OutlinedButton(
                        onClick = {
                            android.widget.Toast.makeText(
                                context, "Google Sign-In coming soon", android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(Ink500, Ink500))),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink100)
                    ) {
                        Text("G", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF4285F4))
                        Spacer(Modifier.width(10.dp))
                        Text("Continue with Google", style = MaterialTheme.typography.titleSmall, color = Ink100)
                    }

                    // Phone Auth
                    OutlinedButton(
                        onClick = {
                            android.widget.Toast.makeText(
                                context, "Phone Auth coming soon", android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(Ink500, Ink500))),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink100)
                    ) {
                        Icon(Icons.Filled.Phone, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Continue with Phone", style = MaterialTheme.typography.titleSmall, color = Ink100)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            AnimatedVisibility(visible = visible, enter = fadeIn(tween(500, 500))) {
                Text(
                    "Your data is end-to-end encrypted 🔒",
                    style = MaterialTheme.typography.labelSmall,
                    color = Ink400,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(40.dp))
        }

        // Loading overlay
        if (authState is AuthState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CircularProgressIndicator(color = Violet400, strokeWidth = 3.dp)
                    Text("Signing you in…", style = MaterialTheme.typography.bodySmall, color = Ink200)
                }
            }
        }

        // Error banner
        AnimatedVisibility(
            visible = authState is AuthState.Error,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CrimsonDim)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("⚠️", fontSize = 16.sp)
                Text(
                    (authState as? AuthState.Error)?.message ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ─── Premium Text Field ───────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = Violet500,
            unfocusedBorderColor = Ink500,
            focusedLabelColor    = Violet400,
            unfocusedLabelColor  = Ink300,
            cursorColor          = Violet400,
            focusedTextColor     = Ink100,
            unfocusedTextColor   = Ink100,
            focusedContainerColor   = Ink800,
            unfocusedContainerColor = Ink800,
        )
    )
}

// ─── No-ripple clickable helper ───────────────────────────────────────
private fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource(),
        indication = null,
        onClick = onClick
    )
