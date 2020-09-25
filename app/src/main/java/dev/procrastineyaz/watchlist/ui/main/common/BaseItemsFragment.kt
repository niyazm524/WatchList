package dev.procrastineyaz.watchlist.ui.main.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.databinding.FragmentHomeBinding
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.helpers.reduceDragSensitivity
import dev.procrastineyaz.watchlist.ui.main.home.HomeFragmentDirections
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_chips.*
import kotlinx.android.synthetic.main.layout_items_view.*

abstract class BaseItemsFragment : Fragment() {
    abstract val vm: HomeViewModel
    abstract val provider: ItemsProviders
    private lateinit var categoryItemPagesAdapter: CategoryItemPagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryItemPagesAdapter =
            CategoryItemPagesAdapter(this, provider)
        vp_movies_lists.adapter = categoryItemPagesAdapter
        TabLayoutMediator(tabLayout, vp_movies_lists) { tab, position ->
            tab.text = getString(
                if (position == 0) R.string.tab_watched else R.string.tab_wish_list
            )
        }.attach()
        vp_movies_lists.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    fab_add.hide()
                } else if (
                    (state == ViewPager2.SCROLL_STATE_SETTLING || state == ViewPager2.SCROLL_STATE_IDLE)
                ) {
                    fab_add.show()
                }
            }

            override fun onPageSelected(position: Int) {
                vm.setCurrentTab(if (position == 0) SeenParameter.SEEN else SeenParameter.UNSEEN)
            }
        })
        vp_movies_lists.reduceDragSensitivity(6)
        cg_categories.setOnCheckedChangeListener { _, checkedId ->
            vm.selectCategory(
                when (checkedId) {
                    R.id.chip_films -> Category.FILM
                    R.id.chip_series -> Category.SERIES
                    R.id.chip_anime -> Category.ANIME
                    else -> Category.UNKNOWN
                }
            )
        }
        cg_categories.check(R.id.chip_all)
        vm.itemForDetails.observe(viewLifecycleOwner) { item ->
            item?.let { (item, extras) ->
                openItemDetails(item, extras)
                vm.navigationToItemCompleted()
            }
        }
    }

    private fun openItemDetails(item: Item, extras: Navigator.Extras) {
        val action = HomeFragmentDirections.actionNavigationHomeToItemDetails(item)
        findNavController().navigate(action)
    }
}
