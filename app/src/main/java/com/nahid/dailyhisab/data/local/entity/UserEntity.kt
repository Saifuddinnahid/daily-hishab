package com.nahid.dailyhisab.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val passwordHash: String,
    val salt: String,
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
