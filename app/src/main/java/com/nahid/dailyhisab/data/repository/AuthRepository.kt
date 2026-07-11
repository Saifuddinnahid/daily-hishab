package com.nahid.dailyhisab.data.repository

import com.nahid.dailyhisab.data.local.dao.UserDao
import com.nahid.dailyhisab.data.local.entity.UserEntity
import com.nahid.dailyhisab.domain.model.User
import com.nahid.dailyhisab.security.PasswordHasher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {
    private var currentUser: User? = null

    fun getCurrentUser(): User? = currentUser

    suspend fun login(email: String, password: String): Result<User> {
        val userEntity = userDao.getUserByEmail(email.lowercase().trim())
            ?: return Result.failure(Exception("ইমেইল বা পাসওয়ার্ড ভুল"))

        val isValid = PasswordHasher.verifyPassword(password, userEntity.salt, userEntity.passwordHash)
        if (!isValid) {
            return Result.failure(Exception("ইমেইল বা পাসওয়ার্ড ভুল"))
        }

        val user = User(
            email = userEntity.email,
            name = userEntity.name,
            createdAt = userEntity.createdAt
        )
        currentUser = user
        return Result.success(user)
    }

    suspend fun register(email: String, password: String, name: String = ""): Result<User> {
        val existingUser = userDao.getUserByEmail(email.lowercase().trim())
        if (existingUser != null) {
            return Result.failure(Exception("এই ইমেইলে ইতিমধ্যে একটি অ্যাকাউন্ট আছে"))
        }

        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hashPassword(password, salt)

        val userEntity = UserEntity(
            email = email.lowercase().trim(),
            passwordHash = hash,
            salt = salt,
            name = name
        )
        userDao.insertUser(userEntity)

        val user = User(
            email = userEntity.email,
            name = userEntity.name,
            createdAt = userEntity.createdAt
        )
        currentUser = user
        return Result.success(user)
    }

    suspend fun changePassword(email: String, currentPassword: String, newPassword: String): Result<Unit> {
        val userEntity = userDao.getUserByEmail(email.lowercase().trim())
            ?: return Result.failure(Exception("ব্যবহারকারী পাওয়া যায়নি"))

        val isValid = PasswordHasher.verifyPassword(currentPassword, userEntity.salt, userEntity.passwordHash)
        if (!isValid) {
            return Result.failure(Exception("বর্তমান পাসওয়ার্ড ভুল"))
        }

        val newSalt = PasswordHasher.generateSalt()
        val newHash = PasswordHasher.hashPassword(newPassword, newSalt)
        userDao.updatePassword(email.lowercase().trim(), newHash, newSalt)
        return Result.success(Unit)
    }

    fun logout() {
        currentUser = null
    }

    suspend fun hasUsers(): Boolean {
        return userDao.getUserCount() > 0
    }
}
