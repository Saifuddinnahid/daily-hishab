package com.nahid.dailyhisab.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nahid.dailyhisab.data.local.dao.CategoryDao
import com.nahid.dailyhisab.data.local.dao.TransactionDao
import com.nahid.dailyhisab.data.local.dao.UserDao
import com.nahid.dailyhisab.data.local.entity.CategoryEntity
import com.nahid.dailyhisab.data.local.entity.TransactionEntity
import com.nahid.dailyhisab.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "daily_hisab_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
