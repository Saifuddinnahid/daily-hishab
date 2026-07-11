package com.nahid.dailyhisab.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordHasher {
    private const val SALT_LENGTH = 32
    private const val HASH_ALGORITHM = "SHA-256"
    private const val ITERATIONS = 10000

    fun generateSalt(): String {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hashPassword(password: String, salt: String): String {
        val saltBytes = Base64.getDecoder().decode(salt)
        var hash = password.toByteArray() + saltBytes
        repeat(ITERATIONS) {
            hash = MessageDigest.getInstance(HASH_ALGORITHM).digest(hash)
        }
        return Base64.getEncoder().encodeToString(hash)
    }

    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val computedHash = hashPassword(password, salt)
        return computedHash == expectedHash
    }
}
