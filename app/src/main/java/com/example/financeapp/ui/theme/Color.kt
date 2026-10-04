package com.example.financeapp.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Brand Palette — Deep Luxury Dark ───────────────────────────────
val Ink900   = Color(0xFF07050F)   // True almost-black base
val Ink800   = Color(0xFF0D0B1A)   // Background
val Ink700   = Color(0xFF16132B)   // Elevated surface
val Ink600   = Color(0xFF1E1A38)   // Card surface
val Ink500   = Color(0xFF2D2847)   // Variant / elevated card
val Ink400   = Color(0xFF433D61)   // Muted element
val Ink300   = Color(0xFF6B638A)   // Tertiary text
val Ink200   = Color(0xFFB8B2CC)   // Secondary text
val Ink100   = Color(0xFFECEAF4)   // Primary text

// ─── Primary Accent — Electric Violet ───────────────────────────────
val Violet900 = Color(0xFF2D0A6B)
val Violet800 = Color(0xFF3D1491)
val Violet700 = Color(0xFF5B21B6)
val Violet600 = Color(0xFF7C3AED)
val Violet500 = Color(0xFF8B5CF6)
val Violet400 = Color(0xFFA78BFA)
val Violet300 = Color(0xFFC4B5FD)
val Violet200 = Color(0xFFDDD6FE)

// ─── Semantic Colors ─────────────────────────────────────────────────
val Emerald   = Color(0xFF10B981)  // Income / positive
val EmeraldDim = Color(0xFF065F46)
val Crimson   = Color(0xFFEF4444)  // Expense / negative
val CrimsonDim = Color(0xFF7F1D1D)
val Amber     = Color(0xFFF59E0B)  // Warning / EMI
val Sapphire  = Color(0xFF3B82F6)  // Accent / invest
val Teal      = Color(0xFF14B8A6)  // Calculator / other
val Rose      = Color(0xFFF43F5E)  // Alternate red

// ─── Category Palette ────────────────────────────────────────────────
val CatFood        = Color(0xFFF97316)
val CatShopping    = Color(0xFFA855F7)
val CatTransport   = Color(0xFF3B82F6)
val CatBills       = Color(0xFFEF4444)
val CatEntertain   = Color(0xFF22C55E)
val CatHealth      = Color(0xFF06B6D4)
val CatEducation   = Color(0xFFF59E0B)
val CatOther       = Color(0xFF94A3B8)

// ─── Gradient Presets ────────────────────────────────────────────────
val HeroPurpleGrad  = listOf(Color(0xFF1A0533), Violet700)
val HeroGreenGrad   = listOf(Color(0xFF022C22), Emerald)
val HeroBlueGrad    = listOf(Color(0xFF0C1445), Sapphire)

// ─── Dark Color Scheme tokens ───────────────────────────────────────
val DarkPrimary            = Violet500
val DarkOnPrimary          = Color.White
val DarkPrimaryContainer   = Violet900
val DarkOnPrimaryContainer = Violet300
val DarkSecondary          = Emerald
val DarkOnSecondary        = Ink800
val DarkSecondaryContainer = EmeraldDim
val DarkOnSecondaryContainer = Color(0xFFD1FAE5)
val DarkTertiary           = Sapphire
val DarkBackground         = Ink800
val DarkSurface            = Ink700
val DarkOnBackground       = Ink100
val DarkOnSurface          = Ink100
val DarkSurfaceVariant     = Ink600
val DarkOnSurfaceVariant   = Ink200
val DarkOutline            = Ink500
val DarkError              = Crimson

// ─── Light Color Scheme tokens ──────────────────────────────────────
val LightPrimary            = Violet700
val LightOnPrimary          = Color.White
val LightPrimaryContainer   = Violet200
val LightOnPrimaryContainer = Violet900
val LightSecondary          = Emerald
val LightOnSecondary        = Color.White
val LightSecondaryContainer = Color(0xFFD1FAE5)
val LightBackground         = Color(0xFFF6F4FF)
val LightSurface            = Color.White
val LightOnBackground       = Color(0xFF0F0B1E)
val LightOnSurface          = Color(0xFF1A1530)
val LightSurfaceVariant     = Color(0xFFEDE9FE)
val LightTertiary           = Sapphire
val LightError              = Crimson

// ─── Legacy / Chart aliases ──────────────────────────────────────────
val PurpleDark   = Ink800
val PurpleVibrant = Violet700
val PurpleMid    = Violet500
val IncomeGreen  = Emerald
val ExpenseRed   = Crimson
val AccentBlue   = Sapphire
val TealAccent   = Teal
val GoldAccent   = Amber
val TextPrimary  = Ink100
val TextSecondary = Ink200

val CategoryFood          = CatFood
val CategoryShopping      = CatShopping
val CategoryTransport     = CatTransport
val CategoryBills         = CatBills
val CategoryEntertainment = CatEntertain
val CategoryHealth        = CatHealth
val CategoryEducation     = CatEducation
val CategoryOther         = CatOther

// Gradient aliases kept for screens that reference them
val GradientPurple = HeroPurpleGrad
val GradientGreen  = HeroGreenGrad
val GradientBlue   = HeroBlueGrad

// Legacy aliases for screens
val Saffron        = Color(0xFFF97316)
val SaffronLight   = Color(0xFFFBBF24)
val DeepBlue       = Color(0xFF1E3A8A)
val DeepBlueLight  = Color(0xFF3B82F6)
val TealLight      = Color(0xFF5EEAD4)
val RoseLight      = Color(0xFFFB7185)
val BackgroundWhite = Color(0xFFF8FAFC)
val SurfaceWhite   = Color(0xFFFFFFFF)
val CardBorder     = Color(0xFFE2E8F0)
val CardBackground = Color(0xFFFFFFFF)
