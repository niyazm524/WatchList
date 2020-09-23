package dev.procrastineyaz.watchlist.ui.main.movies

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.SeenParameter
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapter
import dev.procrastineyaz.watchlist.ui.main.common.ItemsAdapterProvider
import dev.procrastineyaz.watchlist.ui.main.home.HomeViewModel
import kotlinx.android.synthetic.main.items_list_fragment.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel

class ItemsListFragment : Fragment() {

    private lateinit var provider: ItemsAdapterProvider
    private val itemsAdapter: ItemsAdapter = get()
    private var seen: SeenParameter = SeenParameter.SEEN

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val seenArgument = arguments?.getBoolean("seen") ?: true
        seen = if (seenArgument) SeenParameter.SEEN else SeenParameter.UNSEEN
        val providerName =
            Providers.valueOf(arguments?.getString("provider") ?: Providers.HomeVM.name)
        provider = when (providerName) {
            Providers.HomeVM -> getSharedViewModel<HomeViewModel>()
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
            setEmptyListMessageShowing(items.isEmpty())
        })
    }

    private fun setEmptyListMessageShowing(showing: Boolean = true) {
        tv_empty_list.visibility = if(showing) View.VISIBLE else View.GONE
    }

    private fun getUriOfDrawable(@DrawableRes drawableId: Int): Uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(drawableId))
        .appendPath(resources.getResourceTypeName(drawableId))
        .appendPath(resources.getResourceEntryName(drawableId))
        .build()

    companion object {
        fun newInstance(seen: Boolean, provider: Providers) = ItemsListFragment().apply {
            arguments = bundleOf("seen" to seen, "provider" to provider.name)
        }

        enum class Providers {
            HomeVM
        }
    }

}
