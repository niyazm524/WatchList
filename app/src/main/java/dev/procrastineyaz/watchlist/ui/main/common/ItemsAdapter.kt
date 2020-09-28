package dev.procrastineyaz.watchlist.ui.main.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Item
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*

typealias ItemClickListener = (item: Item, container: View) -> Unit

class ItemsAdapter : PagedListAdapter<Item, MovieViewHolder>(MoviesDiffCallback()) {

    var onItemClickListener: ItemClickListener? = null

    init {
        //setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.also { holder.bindTo(it, onItemClickListener) } ?: holder.clear()
    }

    override fun getItemId(position: Int) = getItem(position)?.id ?: -1

}


class MovieViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bindTo(item: Item, onItemClickListener: ItemClickListener? = null) {
        if (onItemClickListener != null) {
            cv_item_root.setOnClickListener { onItemClickListener(item, containerView) }
        }
        bindToItem(item)
    }

    fun clear() {
        iv_poster.setImageResource(R.drawable.no_movie_poster)
        tv_note.text = ""
        tv_rating.visibility = View.INVISIBLE
        cv_item_root.setOnClickListener(null)
        tv_title_global.text = ""
        tv_title.text = "Фильм загружается..."
    }
}

fun LayoutContainer.bindToItem(item: Item) {
    tv_title.text = item.nameRu
    tv_title_global.text = item.nameEn
    val rating = item.rating ?: ""
    if (rating.isEmpty()) {
        tv_rating.backgroundTintList
        tv_rating.visibility = View.INVISIBLE
    } else {
        val hue = rating.toIntOrNull()?.let {
            (it / 10f * 90).coerceIn(0f..90f)
        } ?: 122f
        val color = Color.HSVToColor(floatArrayOf(hue, .95f, .8f))
        ViewCompat.setBackgroundTintList(tv_rating, ColorStateList.valueOf(color))
        tv_rating.visibility = View.VISIBLE
        tv_rating.text = rating
    }

    if (item.note != null) {
        tv_note.text = "\uD83D\uDCDD\t" + item.note
    } else {
        tv_note.text = item.description
    }

    iv_poster.load(item.posterUrl) {
        placeholder(R.drawable.no_movie_poster)
        error(R.drawable.no_movie_poster)
        memoryCacheKey(item.id.toString())
        crossfade(true)
    }
}

class MoviesDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}
