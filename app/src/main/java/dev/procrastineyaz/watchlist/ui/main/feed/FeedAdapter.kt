package dev.procrastineyaz.watchlist.ui.main.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.FeedItem
import dev.procrastineyaz.watchlist.ui.main.common.bindToItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_feed.*
import kotlinx.android.synthetic.main.item_movie.*

typealias FeedItemClickListener = (feedItem: FeedItem) -> Unit

class FeedAdapter : PagedListAdapter<FeedItem, FeedItemViewHolder>(FeedItemDiffCallback()) {
    var onItemClickListener: FeedItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FeedItemViewHolder(inflater.inflate(R.layout.item_feed, parent, false))
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        getItem(position)?.also { holder.bindTo(it, onItemClickListener) } ?: holder.clear()
    }
}

class FeedItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bindTo(feedItem: FeedItem, listener: FeedItemClickListener?) {
        if (listener != null) {
            cv_item_root.setOnClickListener { listener(feedItem) }
        }
        tv_feed_text.text = feedItem.getFeedText()
        bindToItem(feedItem.item)
    }

    fun clear() {

    }
}

class FeedItemDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem) = oldItem == newItem
}
