package dev.procrastineyaz.watchlist.data.local.dao

import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.ItemsQuery
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity
import dev.procrastineyaz.watchlist.data.local.entities.ItemEntity_
import io.objectbox.Box
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query
import io.objectbox.query.Query

class ItemsDao(private val itemsBox: Box<ItemEntity>) {
    fun getItems(query: ItemsQuery): Query<ItemEntity> = itemsBox.query {
        if (query.category !== Category.UNKNOWN) {
            equal(ItemEntity_.categoryId, query.category.value)
        }
        if (query.filter != SeenParameter.ANY) {
            equal(ItemEntity_.seen, query.filter == SeenParameter.SEEN)
        }
        if (query.search != null) {
            contains(ItemEntity_.nameRu, query.search)
                .or().contains(ItemEntity_.nameEn, query.search)
        }
    }

}
