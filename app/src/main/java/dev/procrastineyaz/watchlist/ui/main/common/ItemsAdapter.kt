package dev.procrastineyaz.watchlist.ui.main.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*

class ItemsAdapter : ListAdapter<Item, MovieViewHolder>(MoviesDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemId(position: Int) = getItem(position).id

}


class MovieViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bindTo(item: Item) {
        tv_title.text = item.nameRu
        tv_title_global.text = item.nameEn
        tv_rating.text = item.rating.toString()
        tv_note.text = "\uD83D\uDCDD\t" + item.note
        Glide
            .with(containerView)
            .load(item.posterUrl)
            .into(iv_poster)
    }
}

class MoviesDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}
