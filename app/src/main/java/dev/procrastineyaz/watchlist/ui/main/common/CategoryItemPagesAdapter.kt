package dev.procrastineyaz.watchlist.ui.main.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.procrastineyaz.watchlist.ui.main.movies.MoviesListFragment

class CategoryItemPagesAdapter(
    fragment: Fragment,
    private val itemsProvider: MoviesListFragment.Companion.Providers
) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MoviesListFragment.newInstance(seen = true, provider = itemsProvider)
        else -> MoviesListFragment.newInstance(seen = false, provider = itemsProvider)
    }

    override fun getItemCount(): Int = 2
}

