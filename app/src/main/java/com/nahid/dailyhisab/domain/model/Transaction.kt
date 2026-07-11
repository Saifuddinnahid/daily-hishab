package com.nahid.dailyhisab.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long? = null,
    val categoryName: String = "",
    val categoryIcon: String = "receipt",
    val categoryColor: Long = 0xFF1E40AF,
    val note: String = "",
    val date: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class TransactionType(val value: String) {
    INCOME("income"),
    EXPENSE("expense");

    companion object {
        fun fromValue(value: String): TransactionType {
            return entries.find { it.value == value } ?: EXPENSE
        }
    }
}
