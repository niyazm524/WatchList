package dev.procrastineyaz.watchlist.data.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


abstract class User {
    abstract val id: Long
    abstract val username: String
    abstract val email: String
}

@Parcelize
@Keep data class SubscribeUser(
    override val id: Long,
    override val username: String,
    override val email: String,
    val avatarUrl: String? = null,
    val isSubscriber: Boolean,
    val approved: Boolean,
    val approvedBack: Boolean,
    val canUnsubscribe: Boolean,
    val subscribedBack: Boolean,
    val canSubscribe: Boolean,
) : User(), Parcelable {
    fun getActionText(): String {
        if(isSubscriber) {
            if(!approved) return "Принять"
            return if(approvedBack) "Подписки"
            else "Подписаться"
        } else {
            if(!approved) return "Заявка отправлена"
            return if(approvedBack) "Подписки"
            else "Отписаться"
        }
    }
}
