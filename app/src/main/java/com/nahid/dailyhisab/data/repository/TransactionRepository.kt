package com.nahid.dailyhisab.data.repository

import com.nahid.dailyhisab.data.local.dao.TransactionDao
import com.nahid.dailyhisab.data.local.entity.TransactionEntity
import com.nahid.dailyhisab.domain.model.Category
import com.nahid.dailyhisab.domain.model.Transaction
import com.nahid.dailyhisab.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryRepository: CategoryRepository
) {
    fun getTransactions(userEmail: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByUser(userEmail).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getTransactionsBetweenDates(
        userEmail: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsBetweenDates(userEmail, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getTransactionsBetweenDatesSync(
        userEmail: String,
        startDate: Long,
        endDate: Long
    ): List<Transaction> {
        return transactionDao.getTransactionsBetweenDatesSync(userEmail, startDate, endDate)
            .map { it.toDomain() }
    }

    suspend fun getTotalIncomeBetweenDates(userEmail: String, startDate: Long, endDate: Long): Double {
        return transactionDao.getTotalIncomeBetweenDates(userEmail, startDate, endDate)
    }

    suspend fun getTotalExpenseBetweenDates(userEmail: String, startDate: Long, endDate: Long): Double {
        return transactionDao.getTotalExpenseBetweenDates(userEmail, startDate, endDate)
    }

    suspend fun saveTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransactionById(id)
    }

    suspend fun getTodayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfDay = cal.timeInMillis

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val endOfDay = cal.timeInMillis

        return Pair(startOfDay, endOfDay)
    }

    suspend fun getWeekRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfWeek = cal.timeInMillis

        cal.add(Calendar.WEEK_OF_YEAR, 1)
        cal.add(Calendar.MILLISECOND, -1)
        val endOfWeek = cal.timeInMillis

        return Pair(startOfWeek, endOfWeek)
    }

    suspend fun getMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfMonth = cal.timeInMillis

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val endOfMonth = cal.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    private suspend fun TransactionEntity.toDomain(): Transaction {
        val category = categoryId?.let { categoryRepository.getCategoryById(it) }
        return Transaction(
            id = id,
            amount = amount,
            type = TransactionType.fromValue(type),
            categoryId = categoryId,
            categoryName = category?.name ?: "",
            categoryIcon = category?.icon ?: "receipt",
            categoryColor = category?.color ?: 0xFF1E40AF,
            note = note,
            date = date,
            createdAt = createdAt
        )
    }

    private fun Transaction.toEntity() = TransactionEntity(
        id = id,
        amount = amount,
        type = type.value,
        categoryId = categoryId,
        note = note,
        date = date,
        createdAt = createdAt
    )
}
