package com.example.financeapp.ui.calculator

import com.example.financeapp.ui.dashboard.DashboardColors
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.calculator.CiResult
import com.example.financeapp.calculator.CompoundInterestCalculator
import com.example.financeapp.calculator.EmiCalculator
import com.example.financeapp.calculator.EmiResult
import com.example.financeapp.calculator.FdCalculator
import com.example.financeapp.calculator.FdResult
import com.example.financeapp.calculator.GstCalculator
import com.example.financeapp.calculator.GstResult
import com.example.financeapp.calculator.InflationCalculator
import com.example.financeapp.calculator.InflationResult
import com.example.financeapp.calculator.LoanComparisonCalculator
import com.example.financeapp.calculator.LoanComparisonResult
import com.example.financeapp.calculator.LoanPayoffCalculator
import com.example.financeapp.calculator.LoanPayoffResult
import com.example.financeapp.calculator.LumpsumCalculator
import com.example.financeapp.calculator.LumpsumResult
import com.example.financeapp.calculator.PpfCalculator
import com.example.financeapp.calculator.PpfResult
import com.example.financeapp.calculator.RateStressTestCalculator
import com.example.financeapp.calculator.RdCalculator
import com.example.financeapp.calculator.RdResult
import com.example.financeapp.calculator.RetirementCalculator
import com.example.financeapp.calculator.RetirementResult
import com.example.financeapp.calculator.SipCalculator
import com.example.financeapp.calculator.SipResult
import com.example.financeapp.calculator.StressTestScenario
import com.example.financeapp.calculator.TaxCalculator
import com.example.financeapp.calculator.TaxResult
import com.example.financeapp.data.BankRatesRepository
import com.example.financeapp.ui.theme.AccentBlue
import com.example.financeapp.ui.theme.Blue400
import com.example.financeapp.ui.theme.Blue500
import com.example.financeapp.ui.theme.Blue600
import com.example.financeapp.ui.theme.ExpenseRed
import com.example.financeapp.ui.theme.IncomeGreen
import com.example.financeapp.ui.theme.FinanceAppTheme
import com.example.financeapp.util.toINR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onBack: () -> Unit, initialTab: Int = 0) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val tabs = listOf("EMI", "SIP", "FD", "RD", "PPF", "Lumpsum", "CI", "Tax", "GST", "Retire", "Payoff", "Inflation", "Loan Compare")

    Scaffold(
        containerColor = DashboardColors.bg(),
        topBar = {
            TopAppBar(
                title = { Text("Financial Calculators", fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DashboardColors.textPrimary())
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DashboardColors.bg(),
                    titleContentColor = DashboardColors.textPrimary(),
                    navigationIconContentColor = DashboardColors.textPrimary()
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DashboardColors.bg())
                .padding(padding)
        ) {
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = DashboardColors.surface(),
                contentColor = Blue400,
                edgePadding = 16.dp,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) Blue400 else DashboardColors.textSecondary()
                            )
                        }
                    )
                }
            }
            when (selectedTab) {
                0 -> EmiCalculatorTab()
                1 -> SipCalculatorTab()
                2 -> FdCalculatorTab()
                3 -> RdCalculatorTab()
                4 -> PpfCalculatorTab()
                5 -> LumpsumCalculatorTab()
                6 -> CiCalculatorTab()
                7 -> TaxCalculatorTab()
                8 -> GstCalculatorTab()
                9 -> RetirementCalculatorTab()
                10 -> LoanPayoffCalculatorTab()
                11 -> InflationCalculatorTab()
                12 -> LoanCompareTab()
            }
        }
    }
}

// ─── Reusable helper ─────────────────────────────────────────────────
@Composable
fun CalcInput(value: String, onValueChange: (String) -> Unit, label: String, isDecimal: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.Number
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue500,
            unfocusedBorderColor = DashboardColors.border(),
            focusedTextColor = DashboardColors.textPrimary(),
            unfocusedTextColor = DashboardColors.textPrimary(),
            focusedLabelColor = Blue400,
            unfocusedLabelColor = DashboardColors.textSecondary(),
            cursorColor = Blue400,
            focusedContainerColor = DashboardColors.surface(),
            unfocusedContainerColor = DashboardColors.surface()
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
    )
}

@Composable
fun CalcButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Blue600),
        shape = RoundedCornerShape(14.dp)
    ) { Text(text, fontWeight = FontWeight.Bold, color = Color.White) }
}

@Composable
fun ResultCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(spring(stiffness = Spring.StiffnessLow)),
        color = DashboardColors.surface(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Blue500.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, color = DashboardColors.textSecondary())
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DashboardColors.textPrimary())
    }
}

// ─── 1. EMI ──────────────────────────────────────────────────────────
@Composable
fun EmiCalculatorTab() {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<EmiResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("EMI Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(principal, { principal = it }, "Loan Amount (₹)")
        CalcInput(rate, { rate = it }, "Annual Rate (%)")
        CalcInput(months, { months = it }, "Tenure (months)", isDecimal = false)
        CalcButton("Calculate EMI") {
            val p = principal.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val m = months.toIntOrNull() ?: return@CalcButton
            result = EmiCalculator.calculate(p, r, m)
        }
        result?.let { r -> ResultCard { ResultRow("Monthly EMI", r.monthlyEmi.toINR()); ResultRow("Total Payment", r.totalPayment.toINR()); ResultRow("Total Interest", r.totalInterest.toINR()) } }
    }
}

// ─── 2. SIP ──────────────────────────────────────────────────────────
@Composable
fun SipCalculatorTab() {
    var monthly by remember { mutableStateOf("") }
    var returnRate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<SipResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("SIP Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(monthly, { monthly = it }, "Monthly Investment (₹)")
        CalcInput(returnRate, { returnRate = it }, "Expected Return (% p.a.)")
        CalcInput(years, { years = it }, "Duration (years)", isDecimal = false)
        CalcButton("Calculate SIP") {
            val m = monthly.toDoubleOrNull() ?: return@CalcButton
            val r = returnRate.toDoubleOrNull() ?: return@CalcButton
            val y = years.toIntOrNull() ?: return@CalcButton
            result = SipCalculator.calculate(m, r, y)
        }
        result?.let { r -> ResultCard { ResultRow("Future Value", r.futureValue.toINR()); ResultRow("Total Invested", r.totalInvested.toINR()); ResultRow("Wealth Gain", r.wealthGain.toINR()) } }
    }
}

// ─── 3. FD ───────────────────────────────────────────────────────────
@Composable
fun FdCalculatorTab() {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<FdResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Fixed Deposit Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Quarterly compounding (standard for Indian banks)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        CalcInput(principal, { principal = it }, "Deposit Amount (₹)")
        CalcInput(rate, { rate = it }, "Annual Interest Rate (%)")
        CalcInput(years, { years = it }, "Tenure (years)", isDecimal = false)
        CalcButton("Calculate FD") {
            val p = principal.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val y = years.toIntOrNull() ?: return@CalcButton
            result = FdCalculator.calculate(p, r, y)
        }
        result?.let { r -> ResultCard { ResultRow("Maturity Amount", r.maturityAmount.toINR()); ResultRow("Interest Earned", r.interestEarned.toINR()) } }
    }
}

// ─── 4. RD ───────────────────────────────────────────────────────────
@Composable
fun RdCalculatorTab() {
    var monthly by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<RdResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Recurring Deposit Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(monthly, { monthly = it }, "Monthly Deposit (₹)")
        CalcInput(rate, { rate = it }, "Annual Interest Rate (%)")
        CalcInput(months, { months = it }, "Duration (months)", isDecimal = false)
        CalcButton("Calculate RD") {
            val m = monthly.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val n = months.toIntOrNull() ?: return@CalcButton
            result = RdCalculator.calculate(m, r, n)
        }
        result?.let { r -> ResultCard { ResultRow("Maturity Amount", r.maturityAmount.toINR()); ResultRow("Interest Earned", r.interestEarned.toINR()) } }
    }
}

// ─── 5. PPF ──────────────────────────────────────────────────────────
@Composable
fun PpfCalculatorTab() {
    var yearly by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("7.1") }
    var years by remember { mutableStateOf("15") }
    var result by remember { mutableStateOf<PpfResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("PPF Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Public Provident Fund — tax-free returns", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        CalcInput(yearly, { yearly = it }, "Yearly Deposit (₹)")
        CalcInput(rate, { rate = it }, "Interest Rate (% p.a.)")
        CalcInput(years, { years = it }, "Duration (years)", isDecimal = false)
        CalcButton("Calculate PPF") {
            val y = yearly.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val n = years.toIntOrNull() ?: return@CalcButton
            result = PpfCalculator.calculate(y, r, n)
        }
        result?.let { r -> ResultCard { ResultRow("Maturity Amount", r.maturityAmount.toINR()); ResultRow("Total Deposited", r.totalDeposited.toINR()); ResultRow("Interest Earned", r.interestEarned.toINR()) } }
    }
}

// ─── 6. Lumpsum ──────────────────────────────────────────────────────
@Composable
fun LumpsumCalculatorTab() {
    var principal by remember { mutableStateOf("") }
    var returnRate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<LumpsumResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Lumpsum Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("One-time investment growth", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        CalcInput(principal, { principal = it }, "Investment Amount (₹)")
        CalcInput(returnRate, { returnRate = it }, "Expected Return (% p.a.)")
        CalcInput(years, { years = it }, "Duration (years)", isDecimal = false)
        CalcButton("Calculate") {
            val p = principal.toDoubleOrNull() ?: return@CalcButton
            val r = returnRate.toDoubleOrNull() ?: return@CalcButton
            val y = years.toIntOrNull() ?: return@CalcButton
            result = LumpsumCalculator.calculate(p, r, y)
        }
        result?.let { r -> ResultCard { ResultRow("Future Value", r.futureValue.toINR()); ResultRow("Total Gain", r.totalGain.toINR()) } }
    }
}

// ─── 7. Compound Interest ────────────────────────────────────────────
@Composable
fun CiCalculatorTab() {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var freq by remember { mutableStateOf("12") }
    var result by remember { mutableStateOf<CiResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Compound Interest", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(principal, { principal = it }, "Principal (₹)")
        CalcInput(rate, { rate = it }, "Annual Rate (%)")
        CalcInput(years, { years = it }, "Duration (years)", isDecimal = false)
        CalcInput(freq, { freq = it }, "Compounding/year (1,2,4,12)", isDecimal = false)
        CalcButton("Calculate CI") {
            val p = principal.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val y = years.toIntOrNull() ?: return@CalcButton
            val f = freq.toIntOrNull()?.coerceAtLeast(1) ?: 12
            result = CompoundInterestCalculator.calculate(p, r, y, f)
        }
        result?.let { r -> ResultCard { ResultRow("Total Amount", r.totalAmount.toINR()); ResultRow("Interest Earned", r.interestEarned.toINR()) } }
    }
}

// ─── 8. Tax ──────────────────────────────────────────────────────────
@Composable
fun TaxCalculatorTab() {
    var income by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<TaxResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Income Tax (New Regime FY24-25)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(income, { income = it }, "Annual Income (₹)")
        CalcButton("Calculate Tax") {
            val i = income.toDoubleOrNull() ?: return@CalcButton
            result = TaxCalculator.calculateNewRegime(i)
        }
        result?.let { r ->
            ResultCard {
                ResultRow("Taxable Income", r.taxableIncome.toINR())
                ResultRow("Income Tax", r.incomeTax.toINR())
                ResultRow("Cess (4%)", r.cess.toINR())
                ResultRow("Total Tax", r.totalTax.toINR())
                ResultRow("Effective Rate", String.format("%.2f%%", r.effectiveRate))
            }
        }
    }
}

// ─── 9. GST ──────────────────────────────────────────────────────────
@Composable
fun GstCalculatorTab() {
    var amount by remember { mutableStateOf("") }
    var gstRate by remember { mutableStateOf("18") }
    var isExclusive by remember { mutableStateOf(true) } // true = add GST, false = remove
    var result by remember { mutableStateOf<GstResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("GST Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(amount, { amount = it }, if (isExclusive) "Amount (excl. GST)" else "Amount (incl. GST)")
        CalcInput(gstRate, { gstRate = it }, "GST Rate (%)")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = isExclusive, onClick = { isExclusive = true }, label = { Text("Add GST") })
            FilterChip(selected = !isExclusive, onClick = { isExclusive = false }, label = { Text("Remove GST") })
        }
        CalcButton("Calculate GST") {
            val a = amount.toDoubleOrNull() ?: return@CalcButton
            val g = gstRate.toDoubleOrNull() ?: return@CalcButton
            result = if (isExclusive) GstCalculator.addGst(a, g) else GstCalculator.removeGst(a, g)
        }
        result?.let { r ->
            ResultCard {
                ResultRow("Base Amount", r.baseAmount.toINR())
                ResultRow("GST (${r.gstPercent.toInt()}%)", r.gstAmount.toINR())
                ResultRow("Total Amount", r.totalAmount.toINR())
            }
        }
    }
}

// ─── 10. Retirement ──────────────────────────────────────────────────
@Composable
fun RetirementCalculatorTab() {
    var expense by remember { mutableStateOf("") }
    var currentAge by remember { mutableStateOf("") }
    var retireAge by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<RetirementResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Retirement Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(expense, { expense = it }, "Current Monthly Expense (₹)")
        CalcInput(currentAge, { currentAge = it }, "Current Age", isDecimal = false)
        CalcInput(retireAge, { retireAge = it }, "Retirement Age", isDecimal = false)
        CalcButton("Calculate") {
            val e = expense.toDoubleOrNull() ?: return@CalcButton
            val ca = currentAge.toIntOrNull() ?: return@CalcButton
            val ra = retireAge.toIntOrNull() ?: return@CalcButton
            result = RetirementCalculator.calculate(e, ca, ra)
        }
        result?.let { r -> ResultCard { ResultRow("Corpus Needed", r.corpusNeeded.toINR()); ResultRow("Future Monthly Expense", r.futureMonthlyExpense.toINR()) } }
    }
}

// ─── 11. Loan Payoff ─────────────────────────────────────────────────
@Composable
fun LoanPayoffCalculatorTab() {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var emi by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<LoanPayoffResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Loan Payoff Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        CalcInput(principal, { principal = it }, "Outstanding Balance (₹)")
        CalcInput(rate, { rate = it }, "Annual Rate (%)")
        CalcInput(emi, { emi = it }, "Current EMI (₹)")
        CalcInput(extra, { extra = it }, "Extra Payment/month (₹)")
        CalcButton("Calculate") {
            val p = principal.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val e = emi.toDoubleOrNull() ?: return@CalcButton
            val ex = extra.toDoubleOrNull() ?: 0.0
            result = LoanPayoffCalculator.calculate(p, r, e, ex)
        }
        result?.let { r ->
            ResultCard {
                if (r.months < 0) {
                    Text("EMI is too low to pay off the loan!", color = ExpenseRed, fontWeight = FontWeight.Bold)
                } else {
                    ResultRow("Months to Payoff", "${r.months}")
                    ResultRow("Total Paid", r.totalPaid.toINR())
                    ResultRow("Total Interest", r.totalInterest.toINR())
                }
            }
        }
    }
}

// ─── 12. Inflation ───────────────────────────────────────────────────
@Composable
fun InflationCalculatorTab() {
    var amount by remember { mutableStateOf("") }
    var inflation by remember { mutableStateOf("6") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<InflationResult?>(null) }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Inflation Calculator", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("See how inflation erodes your money's value", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        CalcInput(amount, { amount = it }, "Current Amount (₹)")
        CalcInput(inflation, { inflation = it }, "Inflation Rate (% p.a.)")
        CalcInput(years, { years = it }, "After (years)", isDecimal = false)
        CalcButton("Calculate") {
            val a = amount.toDoubleOrNull() ?: return@CalcButton
            val i = inflation.toDoubleOrNull() ?: return@CalcButton
            val y = years.toIntOrNull() ?: return@CalcButton
            result = InflationCalculator.calculate(a, i, y)
        }
        result?.let { r ->
            ResultCard {
                ResultRow("You'll need", r.futureEquivalent.toINR())
                ResultRow("Today's ₹ will feel like", r.presentValue.toINR())
            }
        }
    }
}

// ─── 13. Loan Compare ───────────────────────────────────────────────────
@Composable
fun LoanCompareTab() {
    var principal by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var tenureMonths by remember { mutableStateOf("") }

    var comparisonResult by remember { mutableStateOf<LoanComparisonResult?>(null) }
    var stressTestScenarios by remember { mutableStateOf<List<StressTestScenario>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Loan Comparison", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Compare Reducing Balance vs Flat Rate, and see Floating Rate stress tests.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        CalcInput(principal, { principal = it }, "Principal Amount (₹)")
        CalcInput(rate, { rate = it }, "Interest Rate (% p.a.)")
        CalcInput(tenureMonths, { tenureMonths = it }, "Tenure (Months)", isDecimal = false)

        CalcButton("Compare Loans") {
            val p = principal.toDoubleOrNull() ?: return@CalcButton
            val r = rate.toDoubleOrNull() ?: return@CalcButton
            val m = tenureMonths.toIntOrNull() ?: return@CalcButton

            comparisonResult = LoanComparisonCalculator.compare(p, r, m)
            stressTestScenarios = RateStressTestCalculator.calculateScenarios(p, r, m)
        }

        comparisonResult?.let { res ->
            Text("Reducing vs Flat Rate", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = IncomeGreen.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Reducing", fontWeight = FontWeight.Bold, color = IncomeGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("EMI: ${res.reducingEmi.toINR()}", fontSize = 14.sp)
                        Text("Total Int: ${res.reducingTotalInterest.toINR()}", fontSize = 14.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = ExpenseRed.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Flat Rate", fontWeight = FontWeight.Bold, color = ExpenseRed)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("EMI: ${res.flatEmi.toINR()}", fontSize = 14.sp)
                        Text("Total Int: ${res.flatTotalInterest.toINR()}", fontSize = 14.sp)
                    }
                }
            }

            Text("Floating Rate Stress Test", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    stressTestScenarios.forEach { scenario ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(scenario.label, fontWeight = if (scenario.extraInterest == 0.0) FontWeight.Bold else FontWeight.Normal)
                            Column(horizontalAlignment = Alignment.End) {
                                Text("EMI: ${scenario.emi.toINR()}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                if (scenario.extraInterest > 0) {
                                    Text("+${scenario.extraInterest.toINR()} extra", fontSize = 12.sp, color = ExpenseRed)
                                }
                            }
                        }
                        if (scenario != stressTestScenarios.last()) HorizontalDivider()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Today's Bank Rates (Bangalore)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        
        val bankRates = BankRatesRepository.getBangaloreRates()
        bankRates.forEach { bank ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { rate = bank.minRate.toString() },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(bank.bankName, fontWeight = FontWeight.Bold)
                        Text(bank.loanType, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${bank.minRate}% - ${bank.maxRate}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("Tap to apply", fontSize = 12.sp, color = AccentBlue)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ─── Previews ─────────────────────────────────────────────────────────

@Preview(name = "Calculator Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CalculatorScreenLightPreview() {
    FinanceAppTheme { CalculatorScreen(onBack = {}) }
}

@Preview(name = "Calculator Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalculatorScreenDarkPreview() {
    FinanceAppTheme { CalculatorScreen(onBack = {}) }
}

@Preview(name = "CalcInput Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun CalcInputLightPreview() {
    FinanceAppTheme { CalcInput(value = "50000", onValueChange = {}, label = "Principal (₹)") }
}

@Preview(name = "CalcInput Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun CalcInputDarkPreview() {
    FinanceAppTheme { CalcInput(value = "50000", onValueChange = {}, label = "Principal (₹)") }
}

@Preview(name = "ResultRow Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun ResultRowLightPreview() {
    FinanceAppTheme { ResultRow(label = "Monthly EMI", value = "₹12,500") }
}

@Preview(name = "ResultRow Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ResultRowDarkPreview() {
    FinanceAppTheme { ResultRow(label = "Monthly EMI", value = "₹12,500") }
}

@Preview(name = "EmiTab Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun EmiCalculatorTabLightPreview() {
    FinanceAppTheme { EmiCalculatorTab() }
}

@Preview(name = "EmiTab Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmiCalculatorTabDarkPreview() {
    FinanceAppTheme { EmiCalculatorTab() }
}

@Preview(name = "SipTab Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SipCalculatorTabLightPreview() {
    FinanceAppTheme { SipCalculatorTab() }
}

@Preview(name = "SipTab Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SipCalculatorTabDarkPreview() {
    FinanceAppTheme { SipCalculatorTab() }
}
