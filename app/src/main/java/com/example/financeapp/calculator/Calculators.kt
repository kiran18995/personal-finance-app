package com.example.financeapp.calculator

import kotlin.math.pow

object EmiCalculator {
    fun calculate(principal: Double, annualRate: Double, months: Int): EmiResult {
        if (principal <= 0 || months <= 0) return EmiResult(0.0, 0.0, 0.0)
        if (annualRate <= 0) return EmiResult(principal / months, principal, 0.0)
        val r = annualRate / 12.0 / 100.0
        val factor = (1 + r).pow(months)
        val emi = principal * r * factor / (factor - 1)
        val totalPayment = emi * months
        val totalInterest = totalPayment - principal
        return EmiResult(emi, totalPayment, totalInterest)
    }
}

data class EmiResult(val monthlyEmi: Double, val totalPayment: Double, val totalInterest: Double)

object SipCalculator {
    fun calculate(monthlyInvestment: Double, annualReturn: Double, years: Int): SipResult {
        if (monthlyInvestment <= 0 || years <= 0) return SipResult(0.0, 0.0, 0.0)
        val months = years * 12
        val totalInvested = monthlyInvestment * months
        if (annualReturn <= 0) return SipResult(totalInvested, totalInvested, 0.0)
        val r = annualReturn / 12.0 / 100.0
        val factor = (1 + r).pow(months)
        val futureValue = monthlyInvestment * ((factor - 1) / r) * (1 + r)
        val wealthGain = futureValue - totalInvested
        return SipResult(futureValue, totalInvested, wealthGain)
    }
}

data class SipResult(val futureValue: Double, val totalInvested: Double, val wealthGain: Double)

object LoanPayoffCalculator {
    fun calculate(principal: Double, annualRate: Double, emi: Double, extraPayment: Double = 0.0): LoanPayoffResult {
        if (principal <= 0) return LoanPayoffResult(0, 0.0, 0.0)
        val r = annualRate / 12.0 / 100.0
        val totalEmi = emi + extraPayment
        var balance = principal
        var months = 0
        var totalPaid = 0.0
        while (balance > 0 && months < 600) {
            val interest = balance * r
            val principalPaid = totalEmi - interest
            if (principalPaid <= 0) return LoanPayoffResult(-1, 0.0, 0.0)
            balance -= principalPaid
            totalPaid += totalEmi
            months++
        }
        return LoanPayoffResult(months, totalPaid, totalPaid - principal)
    }
}

data class LoanPayoffResult(val months: Int, val totalPaid: Double, val totalInterest: Double)

object RetirementCalculator {
    fun calculate(
        monthlyExpense: Double, currentAge: Int, retireAge: Int,
        lifeExpectancy: Int = 80, inflationRate: Double = 6.0, postRetireReturn: Double = 7.0
    ): RetirementResult {
        val yearsToRetire = retireAge - currentAge
        val yearsInRetirement = lifeExpectancy - retireAge
        if (yearsToRetire <= 0 || yearsInRetirement <= 0) return RetirementResult(0.0, 0.0)
        val inflatedMonthly = monthlyExpense * (1 + inflationRate / 100).pow(yearsToRetire)
        val inflatedAnnual = inflatedMonthly * 12
        val realReturn = ((1 + postRetireReturn / 100) / (1 + inflationRate / 100)) - 1
        val corpus = if (realReturn > 0) {
            inflatedAnnual * (1 - (1 + realReturn).pow(-yearsInRetirement)) / realReturn
        } else {
            inflatedAnnual * yearsInRetirement
        }
        return RetirementResult(corpus, inflatedMonthly)
    }
}

data class RetirementResult(val corpusNeeded: Double, val futureMonthlyExpense: Double)

object TaxCalculator {
    fun calculateNewRegime(annualIncome: Double): TaxResult {
        val standardDeduction = 75000.0
        val taxableIncome = maxOf(0.0, annualIncome - standardDeduction)
        val tax = when {
            taxableIncome <= 400000 -> 0.0
            taxableIncome <= 800000 -> (taxableIncome - 400000) * 0.05
            taxableIncome <= 1200000 -> 20000 + (taxableIncome - 800000) * 0.10
            taxableIncome <= 1600000 -> 60000 + (taxableIncome - 1200000) * 0.15
            taxableIncome <= 2000000 -> 120000 + (taxableIncome - 1600000) * 0.20
            taxableIncome <= 2400000 -> 200000 + (taxableIncome - 2000000) * 0.25
            else -> 300000 + (taxableIncome - 2400000) * 0.30
        }
        val finalTax = if (taxableIncome <= 800000) 0.0 else tax
        val cess = finalTax * 0.04
        val totalTax = finalTax + cess
        val effectiveRate = if (annualIncome > 0) totalTax / annualIncome * 100 else 0.0
        return TaxResult(taxableIncome, finalTax, cess, totalTax, effectiveRate)
    }
}

data class TaxResult(val taxableIncome: Double, val incomeTax: Double, val cess: Double, val totalTax: Double, val effectiveRate: Double)

// ──────────────────────────────────────────────────────────────────────
// NEW CALCULATORS
// ──────────────────────────────────────────────────────────────────────

object FdCalculator {
    /** Fixed Deposit — quarterly compounding (most Indian banks) */
    fun calculate(principal: Double, annualRate: Double, years: Int): FdResult {
        if (principal <= 0 || years <= 0 || annualRate <= 0) return FdResult(principal, 0.0)
        val n = 4.0 // quarterly
        val maturity = principal * (1 + annualRate / (n * 100)).pow(n * years)
        return FdResult(maturity, maturity - principal)
    }
}

data class FdResult(val maturityAmount: Double, val interestEarned: Double)

object RdCalculator {
    /** Recurring Deposit — quarterly compounding */
    fun calculate(monthlyDeposit: Double, annualRate: Double, months: Int): RdResult {
        if (monthlyDeposit <= 0 || months <= 0 || annualRate <= 0) return RdResult(monthlyDeposit * months, 0.0)
        val r = annualRate / 400.0 // quarterly rate
        var maturity = 0.0
        for (i in 1..months) {
            val remainingQuarters = (months - i + 1) / 3.0
            maturity += monthlyDeposit * (1 + r).pow(remainingQuarters)
        }
        val totalDeposited = monthlyDeposit * months
        return RdResult(maturity, maturity - totalDeposited)
    }
}

data class RdResult(val maturityAmount: Double, val interestEarned: Double)

object LumpsumCalculator {
    /** One-time investment growth (annual compounding) */
    fun calculate(principal: Double, annualReturn: Double, years: Int): LumpsumResult {
        if (principal <= 0 || years <= 0) return LumpsumResult(principal, 0.0)
        if (annualReturn <= 0) return LumpsumResult(principal, 0.0)
        val maturity = principal * (1 + annualReturn / 100).pow(years)
        return LumpsumResult(maturity, maturity - principal)
    }
}

data class LumpsumResult(val futureValue: Double, val totalGain: Double)

object PpfCalculator {
    /** PPF — annual contribution, 15 year lock-in, annual compounding */
    fun calculate(yearlyDeposit: Double, annualRate: Double = 7.1, years: Int = 15): PpfResult {
        if (yearlyDeposit <= 0 || years <= 0) return PpfResult(0.0, 0.0, 0.0)
        val r = annualRate / 100.0
        var balance = 0.0
        for (i in 1..years) {
            balance = (balance + yearlyDeposit) * (1 + r)
        }
        val totalDeposited = yearlyDeposit * years
        return PpfResult(balance, totalDeposited, balance - totalDeposited)
    }
}

data class PpfResult(val maturityAmount: Double, val totalDeposited: Double, val interestEarned: Double)

object CompoundInterestCalculator {
    /** Generic CI = P(1 + r/n)^(nt) */
    fun calculate(principal: Double, annualRate: Double, years: Int, compoundingFreq: Int = 12): CiResult {
        if (principal <= 0 || years <= 0 || annualRate <= 0) return CiResult(principal, 0.0)
        val amount = principal * (1 + annualRate / (compoundingFreq * 100.0)).pow(compoundingFreq * years)
        return CiResult(amount, amount - principal)
    }
}

data class CiResult(val totalAmount: Double, val interestEarned: Double)

object GstCalculator {
    fun addGst(amount: Double, gstPercent: Double): GstResult {
        val gst = amount * gstPercent / 100.0
        return GstResult(amount, gst, amount + gst, gstPercent)
    }
    fun removeGst(amountInclGst: Double, gstPercent: Double): GstResult {
        val base = amountInclGst * 100.0 / (100.0 + gstPercent)
        val gst = amountInclGst - base
        return GstResult(base, gst, amountInclGst, gstPercent)
    }
}

data class GstResult(val baseAmount: Double, val gstAmount: Double, val totalAmount: Double, val gstPercent: Double)

object InflationCalculator {
    /** What will ₹X be worth in N years at given inflation? */
    fun calculate(currentAmount: Double, inflationRate: Double, years: Int): InflationResult {
        if (currentAmount <= 0 || years <= 0) return InflationResult(currentAmount, currentAmount)
        val futureValue = currentAmount * (1 + inflationRate / 100).pow(years)
        val reducedValue = currentAmount / (1 + inflationRate / 100).pow(years)
        return InflationResult(futureValue, reducedValue)
    }
}

data class InflationResult(
    val futureEquivalent: Double,  // How much you need in future to have same purchasing power
    val presentValue: Double       // What your money will be worth in today's terms
)

object LoanComparisonCalculator {
    fun compare(principal: Double, annualRate: Double, months: Int): LoanComparisonResult {
        // Reducing Balance Method (Standard EMI)
        val reducingResult = EmiCalculator.calculate(principal, annualRate, months)

        // Flat Rate Method
        // Interest = Principal * Rate * Years
        val years = months / 12.0
        val flatTotalInterest = principal * (annualRate / 100.0) * years
        val flatTotalPayment = principal + flatTotalInterest
        val flatMonthlyEmi = if (months > 0) flatTotalPayment / months else 0.0

        return LoanComparisonResult(
            reducingEmi = reducingResult.monthlyEmi,
            reducingTotalInterest = reducingResult.totalInterest,
            reducingTotalPayment = reducingResult.totalPayment,
            flatEmi = flatMonthlyEmi,
            flatTotalInterest = flatTotalInterest,
            flatTotalPayment = flatTotalPayment
        )
    }
}

data class LoanComparisonResult(
    val reducingEmi: Double,
    val reducingTotalInterest: Double,
    val reducingTotalPayment: Double,
    val flatEmi: Double,
    val flatTotalInterest: Double,
    val flatTotalPayment: Double
)

object RateStressTestCalculator {
    fun calculateScenarios(principal: Double, baseRate: Double, months: Int): List<StressTestScenario> {
        val base = EmiCalculator.calculate(principal, baseRate, months)
        val scenarios = mutableListOf<StressTestScenario>()
        scenarios.add(StressTestScenario("Current ($baseRate%)", base.monthlyEmi, base.totalInterest, 0.0))
        
        val ratesToTest = listOf(baseRate + 0.5, baseRate + 1.0, baseRate + 2.0)
        for (rate in ratesToTest) {
            val res = EmiCalculator.calculate(principal, rate, months)
            scenarios.add(StressTestScenario("+${String.format("%.1f", rate - baseRate)}% ($rate%)", res.monthlyEmi, res.totalInterest, res.totalInterest - base.totalInterest))
        }
        return scenarios
    }
}

data class StressTestScenario(
    val label: String,
    val emi: Double,
    val totalInterest: Double,
    val extraInterest: Double
)
