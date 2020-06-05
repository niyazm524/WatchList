package dev.procrastineyaz.watchlist.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*

class FilmsAdapter : ListAdapter<Movie, MovieViewHolder>(MoviesDiffCallback()) {

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

    fun bindTo(movie: Movie) {
        tv_title.text = movie.title
        tv_title_global.text = movie.titleGlobal
        tv_rating.text = movie.rating.toString()
        tv_note.text = "\uD83D\uDCDD\t" + movie.note
        Glide
            .with(containerView)
            .load(movie.posterUri)
            .into(iv_poster)
    }
}

class MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
}
