package dev.procrastineyaz.watchlist.ui.main.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders

class CategoryItemPagesAdapter(
    fragment: Fragment,
    private val itemsProvider: ItemsProviders
) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ItemsListFragment.newInstance(seen = true, provider = itemsProvider)
        else -> ItemsListFragment.newInstance(seen = false, provider = itemsProvider)
    }

    override fun getItemCount(): Int = 2
}

