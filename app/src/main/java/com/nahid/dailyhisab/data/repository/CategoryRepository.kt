package com.nahid.dailyhisab.data.repository

import com.nahid.dailyhisab.data.local.dao.CategoryDao
import com.nahid.dailyhisab.data.local.entity.CategoryEntity
import com.nahid.dailyhisab.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getCategories(userEmail: String): Flow<List<Category>> {
        return categoryDao.getCategoriesByUser(userEmail).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getCategoriesByType(type: String, userEmail: String): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type, userEmail).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    suspend fun saveCategory(category: Category): Long {
        return categoryDao.insertCategory(category.toEntity())
    }

    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    suspend fun deleteCategory(id: Long) {
        categoryDao.deleteCategoryById(id)
    }

    suspend fun getDefaultCategories(): List<Category> {
        return categoryDao.getDefaultCategories().map { it.toDomain() }
    }

    suspend fun initializeDefaultCategories(userEmail: String) {
        val count = categoryDao.getCategoryCountByUser(userEmail)
        if (count > 0) return

        val defaults = listOf(
            CategoryEntity(0, "খাবার", "restaurant", 0xFFE53935, "expense", true, 1, userEmail),
            CategoryEntity(0, "পরিবহন", "directions_bus", 0xFFFB8C00, "expense", true, 2, userEmail),
            CategoryEntity(0, "বাসা", "home", 0xFF8E24AA, "expense", true, 3, userEmail),
            CategoryEntity(0, "ইউটিলিটি", "bolt", 0xFF00897B, "expense", true, 4, userEmail),
            CategoryEntity(0, "বিনোদন", "movie", 0xFF3949AB, "expense", true, 5, userEmail),
            CategoryEntity(0, "শপিং", "shopping_cart", 0xFFC0CA33, "expense", true, 6, userEmail),
            CategoryEntity(0, "স্বাস্থ্য", "local_hospital", 0xFF43A047, "expense", true, 7, userEmail),
            CategoryEntity(0, "শিক্ষা", "school", 0xFF5E35B1, "expense", true, 8, userEmail),
            CategoryEntity(0, "বেতন", "work", 0xFF00ACC1, "income", true, 1, userEmail),
            CategoryEntity(0, "ফ্রিল্যান্স", "computer", 0xFFFF7043, "income", true, 2, userEmail),
            CategoryEntity(0, "ব্যবসা", "store", 0xFF7CB342, "income", true, 3, userEmail),
            CategoryEntity(0, "বিনিয়োগ", "trending_up", 0xFF1E88E5, "income", true, 4, userEmail),
            CategoryEntity(0, "অন্যান্য আয়", "attach_money", 0xFF6D4C41, "income", true, 5, userEmail),
            CategoryEntity(0, "অন্যান্য ব্যয়", "more_horiz", 0xFF78909C, "expense", true, 9, userEmail)
        )

        for (cat in defaults) {
            categoryDao.insertCategory(cat)
        }
    }

    private fun CategoryEntity.toDomain() = Category(
        id = id,
        name = name,
        icon = icon,
        color = color,
        type = type,
        isDefault = isDefault,
        orderIndex = orderIndex
    )

    private fun Category.toEntity() = CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        color = color,
        type = type,
        isDefault = isDefault,
        orderIndex = orderIndex
    )
}
