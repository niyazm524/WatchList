package dev.procrastineyaz.watchlist.data.local.typeconverters

import androidx.room.TypeConverter
import dev.procrastineyaz.watchlist.data.local.entities.Category

class CategoryConverter {
    @TypeConverter
    fun fromCategory(category: Category): Int = category.value

    @TypeConverter
    fun toCategory(categoryId: Int): Category = Category.fromId(categoryId)
}
