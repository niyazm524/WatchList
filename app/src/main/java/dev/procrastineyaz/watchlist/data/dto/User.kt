package dev.procrastineyaz.watchlist.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


abstract class User {
    abstract val id: Long
    abstract val username: String
    abstract val email: String
}

@Parcelize
data class SubscribeUser(
    override val id: Long,
    override val username: String,
    override val email: String,
    val approved: Boolean,
    val avatarUrl: String? = null,
    val canUnsubscribe: Boolean
) : User(), Parcelable
