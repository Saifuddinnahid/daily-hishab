package com.nahid.dailyhisab.domain.model

data class User(
    val email: String,
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
