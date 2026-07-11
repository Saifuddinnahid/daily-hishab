package com.nahid.dailyhisab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahid.dailyhisab.data.repository.CategoryRepository
import com.nahid.dailyhisab.data.repository.TransactionRepository
import com.nahid.dailyhisab.domain.model.Category
import com.nahid.dailyhisab.domain.model.Transaction
import com.nahid.dailyhisab.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _userEmail = MutableStateFlow("nahid@dailyhisab.app")

    val transactions: StateFlow<List<Transaction>> = transactionRepository.getTransactions(_userEmail.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = categoryRepository.getCategories(_userEmail.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incomeCategories: StateFlow<List<Category>> = categoryRepository.getCategoriesByType("income", _userEmail.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenseCategories: StateFlow<List<Category>> = categoryRepository.getCategoriesByType("expense", _userEmail.value)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    init {
        loadTotals()
    }

    fun addTransaction(amount: Double, type: TransactionType, categoryId: Long?, note: String, date: Long) {
        viewModelScope.launch {
            transactionRepository.saveTransaction(
                Transaction(
                    amount = amount,
                    type = type,
                    categoryId = categoryId,
                    note = note,
                    date = date
                )
            )
            loadTotals()
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(id)
            loadTotals()
        }
    }

    fun getRecentTransactions(): List<Transaction> {
        return transactions.value.take(5)
    }

    private fun loadTotals() {
        viewModelScope.launch {
            val (start, end) = transactionRepository.getMonthRange()
            _totalIncome.value = transactionRepository.getTotalIncomeBetweenDates(_userEmail.value, start, end)
            _totalExpense.value = transactionRepository.getTotalExpenseBetweenDates(_userEmail.value, start, end)
        }
    }

    fun getReportTransactions(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _totalIncome.value = transactionRepository.getTotalIncomeBetweenDates(_userEmail.value, startDate, endDate)
            _totalExpense.value = transactionRepository.getTotalExpenseBetweenDates(_userEmail.value, startDate, endDate)
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.saveCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(id)
        }
    }
}
