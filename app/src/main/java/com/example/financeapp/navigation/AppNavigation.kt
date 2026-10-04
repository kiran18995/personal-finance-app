package com.example.financeapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.financeapp.data.FinanceRepository
import com.example.financeapp.data.PdfParserService
import com.example.financeapp.ui.auth.AuthViewModel
import com.example.financeapp.ui.auth.AuthState
import com.example.financeapp.ui.auth.LoginScreen
import com.example.financeapp.ui.calculator.CalculatorScreen
import com.example.financeapp.ui.cards.AddBankAccountScreen
import com.example.financeapp.ui.cards.AddCardScreen
import com.example.financeapp.ui.cards.CardsScreen
import com.example.financeapp.ui.dashboard.DashboardScreen
import com.example.financeapp.ui.dashboard.DashboardViewModel
import com.example.financeapp.ui.emi.EmiScreen
import com.example.financeapp.ui.expense.ExpenseScreen
import com.example.financeapp.ui.history.HistoryScreen
import com.example.financeapp.ui.income.IncomeScreen
import com.example.financeapp.ui.investment.InvestmentScreen
import com.example.financeapp.ui.profile.ProfileScreen
import com.example.financeapp.ui.statement.ImportStatementScreen
import com.example.financeapp.ui.statement.StatementViewModel
import com.example.financeapp.ui.transaction.AddTransactionScreen
import com.example.financeapp.ui.transaction.ChooseAccountScreen
import com.example.financeapp.ui.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val INCOME = "income"
    const val EXPENSE = "expense"
    const val EMI = "emi"
    const val INVESTMENT = "investment"
    const val CALCULATOR = "calculator"
    const val CALCULATOR_LOAN_COMPARE = "calculator_loan_compare"
    const val IMPORT_STATEMENT = "import_statement"
    const val ADD_TRANSACTION = "add_transaction"
    const val CHOOSE_ACCOUNT = "choose_account"
    const val HISTORY = "history"
    const val CARDS = "cards"
    const val PROFILE = "profile"
    const val ADD_BANK_ACCOUNT = "add_bank_account"
    const val ADD_CARD = "add_card"
}

private val bottomNavRoutes = setOf(
    Routes.DASHBOARD, Routes.HISTORY, Routes.CARDS, Routes.PROFILE
)

// ─── Transition helpers ───────────────────────────────────────────────
private fun slideInFromRight(): EnterTransition =
    slideInHorizontally(animationSpec = tween(320, easing = FastOutSlowInEasing)) { it / 3 } +
            fadeIn(animationSpec = tween(220, 60))

private fun slideOutToLeft(): ExitTransition =
    slideOutHorizontally(animationSpec = tween(320, easing = FastOutSlowInEasing)) { -it / 3 } +
            fadeOut(animationSpec = tween(220))

private fun slideInFromLeft(): EnterTransition =
    slideInHorizontally(animationSpec = tween(320, easing = FastOutSlowInEasing)) { -it / 3 } +
            fadeIn(animationSpec = tween(220, 60))

private fun slideOutToRight(): ExitTransition =
    slideOutHorizontally(animationSpec = tween(320, easing = FastOutSlowInEasing)) { it / 3 } +
            fadeOut(animationSpec = tween(220))

private fun tabFadeIn(): EnterTransition =
    fadeIn(animationSpec = tween(280, easing = LinearOutSlowInEasing))

private fun tabFadeOut(): ExitTransition =
    fadeOut(animationSpec = tween(220, easing = FastOutLinearInEasing))

@Composable
fun AppNavigation(
    repo: FinanceRepository,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dashboardViewModel = remember { DashboardViewModel(repo) }
    val statementViewModel = remember { StatementViewModel(repo, PdfParserService(context)) }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val finalStartDestination = if (authState is AuthState.Success) Routes.DASHBOARD else Routes.LOGIN
    val startDestination = finalStartDestination

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val isBottomNavRoute = currentRoute in bottomNavRoutes
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedGlassBackground()
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {


                // ─── Login ──────────────────────────────────────────
                composable(
                    Routes.LOGIN,
                    enterTransition = { fadeIn(tween(400)) },
                    exitTransition = { fadeOut(tween(300)) }
                ) {
                    LoginScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    )
                }

                // ─── Dashboard ──────────────────────────────────────
                composable(
                    Routes.DASHBOARD,
                    enterTransition = { tabFadeIn() },
                    exitTransition = { tabFadeOut() },
                    popEnterTransition = { tabFadeIn() },
                    popExitTransition = { tabFadeOut() }
                ) {
                    DashboardScreen(
                        viewModel = dashboardViewModel,
                        onNavigateToIncome = { navController.navigate(Routes.INCOME) },
                        onNavigateToExpense = { navController.navigate(Routes.EXPENSE) },
                        onNavigateToEmi = { navController.navigate(Routes.EMI) },
                        onNavigateToInvestment = { navController.navigate(Routes.INVESTMENT) },
                        onNavigateToCalculator = { navController.navigate(Routes.CALCULATOR) },
                        onNavigateToLoanCompare = { navController.navigate(Routes.CALCULATOR_LOAN_COMPARE) },
                        onNavigateToImportStatement = { navController.navigate(Routes.IMPORT_STATEMENT) },
                        onNavigateToAddBankAccount = { navController.navigate(Routes.ADD_BANK_ACCOUNT) },
                        onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                        onNavigateToAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) },
                        onEditCard = { card ->
                            dashboardViewModel.selectedCard = card
                            navController.navigate(Routes.ADD_CARD)
                        }
                    )
                }

                // ─── Detail screens ─────────────────────────────────
                composable(Routes.INCOME) {
                    IncomeScreen(repo = repo, onBack = { navController.popBackStack() })
                }
                composable(Routes.EXPENSE) {
                    ExpenseScreen(repo = repo, onBack = { navController.popBackStack() })
                }
                composable(Routes.EMI) {
                    EmiScreen(repo = repo, onBack = { navController.popBackStack() })
                }
                composable(Routes.INVESTMENT) {
                    InvestmentScreen(repo = repo, onBack = { navController.popBackStack() })
                }
                composable(Routes.CALCULATOR) {
                    CalculatorScreen(onBack = { navController.popBackStack() })
                }
                composable(Routes.CALCULATOR_LOAN_COMPARE) {
                    CalculatorScreen(onBack = { navController.popBackStack() }, initialTab = 12)
                }
                composable(Routes.IMPORT_STATEMENT) {
                    ImportStatementScreen(
                        viewModel = statementViewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                // ─── Add Transaction: slide up ──────────────────────
                composable(
                    Routes.ADD_TRANSACTION,
                    enterTransition = {
                        slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(tween(300))
                    },
                    exitTransition = { fadeOut(tween(220)) },
                    popEnterTransition = { fadeIn(tween(200)) },
                    popExitTransition = {
                        slideOutVertically(tween(300)) { it / 2 } + fadeOut(tween(220))
                    }
                ) { backStackEntry ->
                    val selectedAccount = backStackEntry.savedStateHandle.get<String>("selected_account")
                    AddTransactionScreen(
                        onDismiss = { navController.popBackStack() },
                        onNavigateToChooseAccount = { navController.navigate(Routes.CHOOSE_ACCOUNT) },
                        onSave = { amount, isIncome, category, account ->
                            dashboardViewModel.addTransaction(amount, isIncome, category, account)
                            navController.popBackStack()
                        },
                        selectedAccount = selectedAccount
                    )
                }

                composable(Routes.CHOOSE_ACCOUNT) {
                    val bankAccounts by dashboardViewModel.bankAccounts.collectAsStateWithLifecycle()
                    ChooseAccountScreen(
                        bankAccounts = bankAccounts,
                        onDismiss = { navController.popBackStack() },
                        onAccountSelected = { accountName ->
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_account", accountName)
                            navController.popBackStack()
                        },
                        onNavigateToAddCard = { navController.navigate(Routes.ADD_CARD) }
                    )
                }

                // ─── Bottom-nav tab screens ─────────────────────────
                composable(
                    Routes.HISTORY,
                    enterTransition = { tabFadeIn() },
                    exitTransition = { tabFadeOut() },
                    popEnterTransition = { tabFadeIn() },
                    popExitTransition = { tabFadeOut() }
                ) {
                    val allTransactions by dashboardViewModel.allTransactions.collectAsStateWithLifecycle()
                    HistoryScreen(
                        transactions = allTransactions,
                        onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) }
                    )
                }
                composable(
                    Routes.CARDS,
                    enterTransition = { tabFadeIn() },
                    exitTransition = { tabFadeOut() },
                    popEnterTransition = { tabFadeIn() },
                    popExitTransition = { tabFadeOut() }
                ) {
                    val bankAccounts by dashboardViewModel.bankAccounts.collectAsStateWithLifecycle()
                    val cards by dashboardViewModel.cards.collectAsStateWithLifecycle()
                    CardsScreen(
                        bankAccounts = bankAccounts,
                        cards = cards,
                        onAddBankAccount = { 
                            dashboardViewModel.selectedBankAccount = null
                            navController.navigate(Routes.ADD_BANK_ACCOUNT) 
                        },
                        onAddCard = { 
                            dashboardViewModel.selectedCard = null
                            navController.navigate(Routes.ADD_CARD) 
                        },
                        onEditBankAccount = { account ->
                            dashboardViewModel.selectedBankAccount = account
                            navController.navigate(Routes.ADD_BANK_ACCOUNT)
                        },
                        onEditCard = { card ->
                            dashboardViewModel.selectedCard = card
                            navController.navigate(Routes.ADD_CARD)
                        }
                    )
                }
                composable(Routes.ADD_BANK_ACCOUNT) {
                    AddBankAccountScreen(
                        existingAccount = dashboardViewModel.selectedBankAccount,
                        onDismiss = {
                            dashboardViewModel.selectedBankAccount = null
                            navController.popBackStack() 
                        },
                        onSave = { account ->
                            dashboardViewModel.addBankAccount(account)
                            dashboardViewModel.selectedBankAccount = null
                            navController.popBackStack()
                        }
                    )
                }
                composable(Routes.ADD_CARD) {
                    AddCardScreen(
                        existingCard = dashboardViewModel.selectedCard,
                        onDismiss = {
                            dashboardViewModel.selectedCard = null
                            navController.popBackStack()
                        },
                        onSave = { card ->
                            dashboardViewModel.addCard(card)
                            dashboardViewModel.selectedCard = null
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    Routes.PROFILE,
                    enterTransition = { tabFadeIn() },
                    exitTransition = { tabFadeOut() },
                    popEnterTransition = { tabFadeIn() },
                    popExitTransition = { tabFadeOut() }
                ) {
                    ProfileScreen(
                        onSignOut = {
                            authViewModel.signOut()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }

            if (isBottomNavRoute) {
                FloatingGlassDock(
                    currentRoute = currentRoute,
                    onNavigate = { targetRoute ->
                        if (currentRoute != targetRoute) {
                            navController.navigate(targetRoute) {
                                popUpTo(Routes.DASHBOARD) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, bottom = 18.dp),
                    onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) }
                )
            }
        }
    }

// ─── Frosted Glass Floating Dock ─────────────────────────────────────
@Composable
private fun FloatingGlassDock(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    onAddTransaction: () -> Unit
) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val dockBg = if (isDark) Color(0xF2141324) else Color(0xF2FFFFFF)
    val dockBorder = if (isDark) Color.White.copy(alpha = 0.18f) else Color.Black.copy(alpha = 0.1f)
    val shadowSpot = if (isDark) Color.Black.copy(alpha = 0.85f) else Color.Gray.copy(alpha = 0.5f)
    val shadowAmbient = if (isDark) Color.Black.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.2f)

    Surface(
        modifier = modifier
            .height(64.dp)
            .shadow(
                elevation = 28.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = shadowSpot,
                ambientColor = shadowAmbient
            ),
        shape = RoundedCornerShape(32.dp),
        color = dockBg, // Ultra sleek frosted glass tint (95% opaque)
        border = BorderStroke(1.dp, dockBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DockItem(
                icon = Icons.Filled.Home,
                contentDescription = "Home",
                isSelected = currentRoute == Routes.DASHBOARD,
                onClick = { onNavigate(Routes.DASHBOARD) }
            )
            DockItem(
                icon = Icons.Filled.History,
                contentDescription = "History",
                isSelected = currentRoute == Routes.HISTORY,
                onClick = { onNavigate(Routes.HISTORY) }
            )
            
            // Center Glowing + Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
                        )
                    )
                    .clickable { onAddTransaction() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Transaction",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            DockItem(
                icon = Icons.Filled.CreditCard,
                contentDescription = "Cards",
                isSelected = currentRoute == Routes.CARDS,
                onClick = { onNavigate(Routes.CARDS) }
            )
            DockItem(
                icon = Icons.Filled.Person,
                contentDescription = "Profile",
                isSelected = currentRoute == Routes.PROFILE,
                onClick = { onNavigate(Routes.PROFILE) }
            )
        }
    }
}

@Composable
private fun DockItem(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val activeTint = if (isDark) Color.White else Color.Black
    val inactiveTint = if (isDark) Color(0xFF7E7D93) else Color(0xFF94A3B8)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) activeTint else inactiveTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(18.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isSelected) activeTint else Color.Transparent)
        )
    }
}
