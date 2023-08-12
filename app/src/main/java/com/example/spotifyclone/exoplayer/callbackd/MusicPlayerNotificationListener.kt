package com.example.spotifyclone.exoplayer.callbackd

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.spotifyclone.constants.NOTIFICATION_ID
import com.example.spotifyclone.exoplayer.musicservice
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MusicPlayerNotificationListener(  private val musicservice:musicservice):PlayerNotificationManager.NotificationListener {

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicservice.apply {
            stopForeground(true)
            isforeground=false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicservice.apply {
            if(ongoing && !isforeground) {
                ContextCompat.startForegroundService(this, Intent(applicationContext,this::class.java))
                startForeground(NOTIFICATION_ID,notification)
                isforeground=true
            }
        }
    }
}