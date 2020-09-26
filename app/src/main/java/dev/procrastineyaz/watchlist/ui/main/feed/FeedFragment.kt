package dev.procrastineyaz.watchlist.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Item
import dev.procrastineyaz.watchlist.data.dto.NetworkState
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {
    private val feedAdapter = FeedAdapter()
    private val vm: FeedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_feed.adapter = feedAdapter
        rv_feed.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        feedAdapter.onItemClickListener = { feedItem ->
            openItem(feedItem.item)
        }
        vm.feedItems.observe(viewLifecycleOwner) { feed ->
            feedAdapter.submitList(feed)
        }
        vm.networkState.observe(viewLifecycleOwner) { state ->
            if(state is NetworkState.Failure) {
                Toast.makeText(context, state.error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        vm.invalidate()
    }

    private fun openItem(item: Item) {
        val action = FeedFragmentDirections.actionNavigationFeedToItemDetails(item = item)
        findNavController().navigate(action)
    }

}
