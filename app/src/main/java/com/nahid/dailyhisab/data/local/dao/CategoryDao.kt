package com.nahid.dailyhisab.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nahid.dailyhisab.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE userEmail = :userEmail ORDER BY orderIndex ASC")
    fun getCategoriesByUser(userEmail: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE type = :type AND userEmail = :userEmail ORDER BY orderIndex ASC")
    fun getCategoriesByType(type: String, userEmail: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)

    @Query("SELECT * FROM categories WHERE isDefault = 1")
    suspend fun getDefaultCategories(): List<CategoryEntity>

    @Query("SELECT COUNT(*) FROM categories WHERE userEmail = :userEmail")
    suspend fun getCategoryCountByUser(userEmail: String): Int
}
