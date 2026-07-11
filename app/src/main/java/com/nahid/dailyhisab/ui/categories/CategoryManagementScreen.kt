package com.nahid.dailyhisab.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.draw.clip
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nahid.dailyhisab.domain.model.Category
import com.nahid.dailyhisab.domain.model.TransactionType
import com.nahid.dailyhisab.ui.MainViewModel
import com.nahid.dailyhisab.ui.theme.ExpenseRed
import com.nahid.dailyhisab.ui.theme.IncomeGreen

@Composable
fun CategoryManagementScreen(
    userEmail: String,
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val incomeCategories = categories.filter { it.type == "income" }
    val expenseCategories = categories.filter { it.type == "expense" }

    var showAddDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var deleteTarget by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "ক্যাটাগরি ব্যবস্থাপনা",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "আপনার আয়-ব্যয়ের ক্যাটাগরি যোগ, সম্পাদনা বা মুছুন",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        CategorySection(
            title = "আয়ের ক্যাটাগরি",
            categories = incomeCategories,
            typeColor = IncomeGreen,
            onAdd = { showAddDialog = true },
            onEdit = { editingCategory = it },
            onDelete = { deleteTarget = it }
        )

        Spacer(Modifier.height(16.dp))

        CategorySection(
            title = "ব্যয়ের ক্যাটাগরি",
            categories = expenseCategories,
            typeColor = ExpenseRed,
            onAdd = { showAddDialog = true },
            onEdit = { editingCategory = it },
            onDelete = { deleteTarget = it }
        )

        Spacer(Modifier.height(80.dp))
    }

    if (showAddDialog) {
        AddEditCategoryDialog(
            category = null,
            onDismiss = { showAddDialog = false },
            onSave = { name, icon, color, type ->
                viewModel.addCategory(Category(name = name, icon = icon, color = color, type = type.value))
            }
        )
    }

    editingCategory?.let { cat ->
        AddEditCategoryDialog(
            category = cat,
            onDismiss = { editingCategory = null },
            onSave = { name, icon, color, type ->
                viewModel.updateCategory(cat.copy(name = name, icon = icon, color = color, type = type.value))
            }
        )
    }

    deleteTarget?.let { cat ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("ক্যাটাগরি মুছুন") },
            text = { Text("\"${cat.name}\" ক্যাটাগরিটি মুছে ফেলবেন? এটি বাতিল করা যাবে না।") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCategory(cat.id)
                    deleteTarget = null
                }) { Text("হ্যাঁ, মুছুন", color = ExpenseRed) }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text("না") }
            }
        )
    }
}

@Composable
private fun CategorySection(
    title: String,
    categories: List<Category>,
    typeColor: Color,
    onAdd: () -> Unit,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = typeColor)
            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = typeColor.copy(alpha = 0.1f),
                    contentColor = typeColor
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = typeColor, modifier = Modifier.size(20.dp))
            }
        }

        if (categories.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("এখনো ক্যাটাগরি নেই", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        categories.forEach { category ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(category.color).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📝", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(category.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                        if (category.isDefault) {
                            Text("ডিফল্ট", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Edit, "সম্পাদনা", tint = MaterialTheme.colorScheme.primary)
                    }
                    if (!category.isDefault) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Delete, "মুছুন", tint = ExpenseRed.copy(alpha = 0.7f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddEditCategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onSave: (String, String, Long, TransactionType) -> Unit
) {
    val isEditing = category != null
    var name by remember { mutableStateOf(category?.name ?: "") }
    var type by remember { mutableStateOf(category?.type ?: "expense") }
    var icon by remember { mutableStateOf(category?.icon ?: "receipt") }
    var color by remember { mutableStateOf(category?.color ?: (if (type == "income") IncomeGreen.value else ExpenseRed.value)) }
    var error by remember { mutableStateOf<String?>(null) }

val icons = listOf("🍽", "🚌", "🏠", "⚡", "🎬", "🛒", "🏥", "🎓", "💼", "💻", "🏪", "📈", "💰", "📋")
val colors = listOf(IncomeGreen.value, ExpenseRed.value, 0xFF1E40AF, 0xFF059669, 0xFF7C3AED, 0xFFEC4899, 0xFFF97316, 0xFF84CC16, 0xFF06B6D4, 0xFFF43F5E)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "ক্যাটাগরি সম্পাদনা" else "নতুন ক্যাটাগরি") },
        text = {
            Column(modifier = Modifier.width(300.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = null },
                    label = { Text("নাম") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { type = "expense" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "expense") ExpenseRed else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (type == "expense") MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("ব্যয়") }
                    Button(
                        onClick = { type = "income" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (type == "income") IncomeGreen else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (type == "income") MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("আয়") }
                }
                Spacer(Modifier.height(12.dp))

                Text("আইকন", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    listOf("🍽", "🚌", "🏠", "⚡", "🎬", "🛒", "🏥", "🎓", "💼", "💻", "🏪", "📈", "💰", "📋").forEach { ic ->
                        val isSelected = icon == ic
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { icon = ic }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(ic, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text("রং", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                    listOf(IncomeGreen.value.toLong(), ExpenseRed.value.toLong(), 0xFF1E40AF.toLong(), 0xFF059669.toLong(), 0xFF7C3AED.toLong(), 0xFFEC4899.toLong(), 0xFFF97316.toLong(), 0xFF84CC16.toLong(), 0xFF06B6D4.toLong(), 0xFFF43F5E.toLong()).forEach { clr ->
                        val isSelected = color == clr
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(clr))
                                .clickable { color = clr }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }

                if (error != null) {
                    Text(error!!, color = ExpenseRed, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.trim().isEmpty()) {
                    return@TextButton
                }
                onSave(name.trim(), "receipt", color.value, if (type == "income") TransactionType.INCOME else TransactionType.EXPENSE)
            }) { Text(if (isEditing) "আপডেট করুন" else "যোগ করুন") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("বাতিল করুন") }
        }
    )
}