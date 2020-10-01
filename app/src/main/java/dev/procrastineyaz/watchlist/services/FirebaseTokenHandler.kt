package dev.procrastineyaz.watchlist.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.procrastineyaz.watchlist.R
import dev.procrastineyaz.watchlist.ui.main.MainActivity


class FirebaseTokenHandler : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.w("TOKEN", token)
    }

    private val TAG = "Firebase_MSG"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        remoteMessage.notification?.let(this::sendNotification)
        Log.d(TAG, "From: " + remoteMessage.from)
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        createNotificationChannel(NotificationChannels.NewSubscriber)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "NOTIFY")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(message.title)
                .setContentText(message.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun createNotificationChannel(channels: NotificationChannels) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channels.id,
                channels.notificationName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = channels.description
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
