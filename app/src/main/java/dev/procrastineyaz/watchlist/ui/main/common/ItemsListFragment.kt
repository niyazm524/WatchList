package dev.procrastineyaz.watchlist.ui.main.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.ui.dto.ItemsProviders
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import dev.procrastineyaz.watchlist.ui.main.user_view.UserViewViewModel
import kotlinx.android.synthetic.main.items_list_fragment.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel

class ItemsListFragment : Fragment() {

    private lateinit var provider: ItemsAdapterProvider
    private lateinit var providerName: ItemsProviders
    private val itemsAdapter: ItemsAdapter = get()
    private var seen: SeenParameter = SeenParameter.SEEN

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val seenArgument = arguments?.getBoolean("seen") ?: true
        seen = if (seenArgument) SeenParameter.SEEN else SeenParameter.UNSEEN
        providerName =
            ItemsProviders.valueOf(
                arguments?.getString("provider") ?: ItemsProviders.HomeItemsVM.name
            )
        provider = when (providerName) {
            ItemsProviders.HomeItemsVM -> getSharedViewModel<HomeViewModel>()
            ItemsProviders.UserItemsVM -> getSharedViewModel<UserViewViewModel>()
        }
        return inflater.inflate(R.layout.items_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_movies.adapter = itemsAdapter
        itemsAdapter.onItemClickListener = { item, container ->
            val extras = FragmentNavigatorExtras(
//                container.findViewById<TextView>(R.id.tv_title) to "tv_title_transition",
//                container.findViewById<TextView>(R.id.tv_title_global) to "tv_title_global_transition",
                container.findViewById<ImageView>(R.id.iv_poster) to "iv_poster_transition",
            )
            provider.onItemClick(item, extras)
        }
        provider.getItemsLiveData(seen).observe(viewLifecycleOwner, { items ->
            itemsAdapter.submitList(items)
            setEmptyListMessageShowing(providerName == ItemsProviders.HomeItemsVM && items.isEmpty())
        })
    }

    private fun setEmptyListMessageShowing(showing: Boolean = true) {
        tv_empty_list.visibility = if (showing) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance(seen: Boolean, provider: ItemsProviders) = ItemsListFragment().apply {
            arguments = bundleOf("seen" to seen, "provider" to provider.name)
        }

    }

}
