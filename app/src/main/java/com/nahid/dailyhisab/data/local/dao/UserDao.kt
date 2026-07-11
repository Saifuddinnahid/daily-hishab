package com.nahid.dailyhisab.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nahid.dailyhisab.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, passwordHash: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("UPDATE users SET passwordHash = :passwordHash, salt = :salt WHERE email = :email")
    suspend fun updatePassword(email: String, passwordHash: String, salt: String)
}
