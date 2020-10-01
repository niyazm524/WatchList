package dev.procrastineyaz.watchlist.services

enum class NotificationChannels(
    val id: String,
    val notificationName: String,
    val description: String,
) {
    NewSubscriber("NEW_SUBSCRIBER", "Подписки", "Новая заявка на подписку")

}
