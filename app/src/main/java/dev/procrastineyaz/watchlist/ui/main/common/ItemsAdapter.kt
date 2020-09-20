package dev.procrastineyaz.watchlist.ui.main.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*

typealias ItemClickListener = (item: Item) -> Unit

class ItemsAdapter : PagedListAdapter<Item, MovieViewHolder>(MoviesDiffCallback()) {

    var onItemClickListener: ItemClickListener? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it, onItemClickListener) }
    }

    override fun getItemId(position: Int) = getItem(position)?.id ?: -1

}


class MovieViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bindTo(item: Item, onItemClickListener: ItemClickListener? = null) {
        if (onItemClickListener != null) {
            cv_item_root.setOnClickListener { onItemClickListener(item) }
        }
        tv_title.text = item.nameRu
        tv_title_global.text = item.nameEn
        if(item.rating.isNullOrEmpty()) {
            tv_rating.visibility = View.INVISIBLE
        } else {
            tv_rating.visibility = View.VISIBLE
            tv_rating.text = item.rating
        }

        if (item.note != null) {
            tv_note.text = "\uD83D\uDCDD\t" + item.note
        } else {
            tv_note.text = item.description
        }

        iv_poster.load(item.posterUrl) {
            placeholder(R.drawable.no_movie_poster)
            memoryCacheKey(item.id.toString())
            crossfade(true)
        }
    }
}

class MoviesDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}
