package com.nahid.dailyhisab.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String = "receipt",
    val color: Long = 0xFF1E40AF,
    val type: String = "expense",
    val isDefault: Boolean = false,
    val orderIndex: Int = 0
)
