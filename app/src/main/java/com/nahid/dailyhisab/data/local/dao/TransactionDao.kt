package com.nahid.dailyhisab.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nahid.dailyhisab.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("""
        SELECT * FROM transactions 
        WHERE userEmail = :userEmail 
        ORDER BY date DESC, id DESC
    """)
    fun getTransactionsByUser(userEmail: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE userEmail = :userEmail AND date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getTransactionsBetweenDates(
        userEmail: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE userEmail = :userEmail AND date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    suspend fun getTransactionsBetweenDatesSync(
        userEmail: String,
        startDate: Long,
        endDate: Long
    ): List<TransactionEntity>

    @Query("""
        SELECT COALESCE(SUM(CASE WHEN type = 'income' THEN amount ELSE 0 END), 0) 
        FROM transactions WHERE userEmail = :userEmail AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalIncomeBetweenDates(userEmail: String, startDate: Long, endDate: Long): Double

    @Query("""
        SELECT COALESCE(SUM(CASE WHEN type = 'expense' THEN amount ELSE 0 END), 0) 
        FROM transactions WHERE userEmail = :userEmail AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalExpenseBetweenDates(userEmail: String, startDate: Long, endDate: Long): Double

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}
