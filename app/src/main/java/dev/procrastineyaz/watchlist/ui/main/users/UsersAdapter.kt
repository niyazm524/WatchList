package dev.procrastineyaz.watchlist.ui.main.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.data.dto.SubscribeUser
import dev.procrastineyaz.watchlist.databinding.ItemUserBinding
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.*

fun interface UserClickListener {
    fun onClick(user: SubscribeUser)
}

class UsersAdapter : PagedListAdapter<SubscribeUser, UserViewHolder>(UsersDiffCallback()) {
    var onUserClickListener: UserClickListener? = null
    var onSubscribeTo: UserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.also { holder.bindTo(it, onUserClickListener) } ?: holder.clear()
    }
}

class UserViewHolder(override val containerView: View, val binding: ItemUserBinding) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bindTo(user: SubscribeUser, listener: UserClickListener?) {
        binding.user = user
        binding.listener = listener
        if(user.avatarUrl != null) {
            iv_avatar.load(user.avatarUrl) {
                transformations(CircleCropTransformation())
            }
        } else {
            iv_avatar.load(R.drawable.sharingan) {
                transformations(CircleCropTransformation())
            }
        }
    }

    fun clear() {

    }
}

class UsersDiffCallback : DiffUtil.ItemCallback<SubscribeUser>() {
    override fun areItemsTheSame(oldUser: SubscribeUser, newUser: SubscribeUser) = oldUser.id == newUser.id
    override fun areContentsTheSame(oldUser: SubscribeUser, newUser: SubscribeUser) = oldUser == newUser
}
