package com.nahid.dailyhisab.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nahid.dailyhisab.domain.model.Transaction
import com.nahid.dailyhisab.ui.components.DateUtils
import com.nahid.dailyhisab.ui.theme.IncomeGreen
import com.nahid.dailyhisab.ui.theme.ExpenseRed

@Composable
fun TransactionListScreen(
    userEmail: String,
    navController: NavController
) {
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTransaction by remember { mutableStateOf<Transaction?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "লেনদেন",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (transactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "কোনো লেনদেন নেই",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "নিচের + বাটনে ক্লিক করে লেনদেন যোগ করুন",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        items(transactions) { transaction ->
            TransactionCard(
                transaction = transaction,
                onEdit = { editingTransaction = it },
                onDelete = { /* delete */ }
            )
        }
    }
}

@Composable
private fun TransactionCard(
    transaction: Transaction,
    onEdit: (Transaction) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (transaction.type.value == "income") IncomeGreen.copy(alpha = 0.15f)
                        else ExpenseRed.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (transaction.categoryIcon) {
                        "restaurant" -> "🍽"
                        "directions_bus" -> "🚌"
                        "home" -> "🏠"
                        "bolt" -> "⚡"
                        "movie" -> "🎬"
                        "shopping_cart" -> "🛒"
                        "local_hospital" -> "🏥"
                        "school" -> "🎓"
                        "work" -> "💼"
                        "computer" -> "💻"
                        "store" -> "🏪"
                        "trending_up" -> "📈"
                        "attach_money" -> "💰"
                        "more_horiz" -> "📋"
                        else -> "📝"
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.categoryName.ifEmpty { "অন্যান্য" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = DateUtils.formatDate(transaction.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = DateUtils.formatAmount(transaction.amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type.value == "income") IncomeGreen else ExpenseRed
                )
                Text(
                    text = if (transaction.type.value == "income") "আয়" else "ব্যয়",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (transaction.type.value == "income") IncomeGreen.copy(alpha = 0.7f) else ExpenseRed.copy(alpha = 0.7f)
                )
            }
        }
    }
}
