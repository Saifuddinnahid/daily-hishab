package com.nahid.dailyhisab.data.repository

import com.nahid.dailyhisab.data.local.dao.UserDao
import com.nahid.dailyhisab.data.local.entity.UserEntity
import com.nahid.dailyhisab.domain.model.User
import com.nahid.dailyhisab.security.PasswordHasher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val categoryRepository: CategoryRepository
) {
    private val defaultEmail = "nahid@dailyhisab.app"

    fun getDefaultUserEmail(): String = defaultEmail

    suspend fun ensureDefaultUser() {
        val existing = userDao.getUserByEmail(defaultEmail)
        if (existing == null) {
            val salt = PasswordHasher.generateSalt()
            val hash = PasswordHasher.hashPassword("default", salt)
            userDao.insertUser(
                UserEntity(
                    email = defaultEmail,
                    passwordHash = hash,
                    salt = salt,
                    name = "Nahid"
                )
            )
            categoryRepository.initializeDefaultCategories(defaultEmail)
        }
    }
}
