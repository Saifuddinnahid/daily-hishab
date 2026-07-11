package com.nahid.dailyhisab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String = "receipt",
    val color: Long = 0xFF1E40AF,
    val type: String = "expense",
    val isDefault: Boolean = false,
    val orderIndex: Int = 0,
    val userEmail: String = ""
)
