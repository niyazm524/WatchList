package dev.procrastineyaz.watchlist.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.ui.dto.AddItemModalState
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsFragment
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class HomeFragment : BaseItemsFragment() {
    val vm: HomeViewModel by sharedViewModel()
    override val baseVM: BaseItemsViewModel
    get() = vm
    override val provider = ItemsProviders.HomeItemsVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_add.visibility = View.VISIBLE
        fab_add.setOnClickListener { vm.onAddNewItem() }
        vm.addItemModalState.observe(viewLifecycleOwner) { state ->
            if(state is AddItemModalState.Opened) {
                openAddItemDialog(state.category, state.seen)
            }
        }
        setFragmentResultListener("adding") { _, result ->
            if(result.getBoolean("succeed", false)) {
                vm.onNewItemAdded()
            }
        }
        vm.itemForDetails.observe(viewLifecycleOwner) { item ->
            item?.let { (item, extras) ->
                openItemDetails(item, extras)
            }
        }
    }

    private fun openItemDetails(item: Item, extras: Navigator.Extras) {
        val action = HomeFragmentDirections.actionNavigationHomeToItemDetails(item)
        findNavController().navigate(action)
    }

    private fun openAddItemDialog(category: Category, seen: Boolean) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationAddItem(category, seen)
        findNavController().navigate(action)
    }

}
