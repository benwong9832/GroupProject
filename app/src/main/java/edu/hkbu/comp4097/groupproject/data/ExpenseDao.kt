package edu.hkbu.comp4097.groupproject.data

import androidx.room.*

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Query("Select * from expense")
    suspend fun getExpense(): List<Expense>

    @Query("Select * from expense where id = :id")
    suspend fun getExpenseByID(id: String): List<Expense>

    @Query("Select * from expense where category = :category")
    suspend fun getExpenseByCategory(category: String): Expense

    @Query("Select * from expense where year = :year and month = :month ORDER BY date DESC")
    suspend fun getExpenseByYearAndMonth(year: Int, month: Int): List<Expense>

    @Query("Select * from expense where year = :year and month = :month and date = :date")
    suspend fun getExpenseByDate(year: Int, month: Int, date: Int): List<Expense>

    @Delete
    suspend fun delete(vararg expense: Expense)

    @Update
    suspend fun update(expense: Expense)
}