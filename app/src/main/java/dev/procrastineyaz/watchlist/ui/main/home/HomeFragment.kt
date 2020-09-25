package dev.procrastineyaz.watchlist.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import dev.procrastineyaz.watchlist.data.dto.Category
import dev.procrastineyaz.watchlist.ui.dto.AddItemModalState
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.main.common.BaseItemsFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named

@ExperimentalCoroutinesApi
class HomeFragment : BaseItemsFragment() {
    override val vm: HomeViewModel by sharedViewModel(named(ItemsProviders.HomeItemsVM))
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
    }

    private fun openAddItemDialog(category: Category, seen: Boolean) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationAddItem(category, seen)
        findNavController().navigate(action)
    }

}
