package com.example.financeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: Income)

    @Delete
    suspend fun delete(income: Income)

    @Query("SELECT * FROM income ORDER BY date DESC")
    fun getAll(): Flow<List<Income>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM income WHERE date >= :startDate")
    fun getTotalSince(startDate: Long): Flow<Double>
}

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expense WHERE date >= :startDate")
    fun getTotalSince(startDate: Long): Flow<Double>

    @Query("SELECT category, SUM(amount) as total FROM expense WHERE date >= :startDate GROUP BY category ORDER BY total DESC")
    fun getCategoryTotals(startDate: Long): Flow<List<CategoryTotal>>
}

data class CategoryTotal(
    val category: String,
    val total: Double
)

@Dao
interface EmiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emi: Emi)

    @Delete
    suspend fun delete(emi: Emi)

    @Query("SELECT * FROM emi ORDER BY nextDueDate ASC")
    fun getAll(): Flow<List<Emi>>

    @Query("SELECT COALESCE(SUM(emiAmount), 0) FROM emi")
    fun getTotalMonthlyEmi(): Flow<Double>
}

@Dao
interface InvestmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(investment: Investment)

    @Delete
    suspend fun delete(investment: Investment)

    @Query("SELECT * FROM investment ORDER BY name ASC")
    fun getAll(): Flow<List<Investment>>

    @Query("SELECT COALESCE(SUM(currentValue), 0) FROM investment")
    fun getTotalValue(): Flow<Double>
}

@Dao
interface BankAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: BankAccount)

    @Delete
    suspend fun delete(account: BankAccount)

    @Query("SELECT * FROM bank_account ORDER BY lastUpdated DESC")
    fun getAll(): Flow<List<BankAccount>>

    @Query("SELECT COALESCE(SUM(currentBalance), 0) FROM bank_account")
    fun getTotalBalance(): Flow<Double>
}

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Delete
    suspend fun delete(card: Card)

    @Query("SELECT * FROM card ORDER BY addedOn DESC")
    fun getAll(): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE cardType = :type ORDER BY addedOn DESC")
    fun getByType(type: String): Flow<List<Card>>
}
