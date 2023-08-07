package com.example.spotifyclone.exoplayer

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaSession2Service.MediaNotification
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spotifyclone.R
import com.example.spotifyclone.constants.NOTIFICATION_CHANNEL_ID
import com.example.spotifyclone.constants.NOTIFICATION_ID
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class MusicNotificationManager(private val context: Context,
sessiontoken:MediaSessionCompat.Token,
notifcationlistener: PlayerNotificationManager.NotificationListener,
 private val  newsongcallback:()->Unit ) {
 private val notificationManager:PlayerNotificationManager
 init {
     val mediacontroller= MediaControllerCompat(context,sessiontoken)
  notificationManager=PlayerNotificationManager.Builder(context
  , NOTIFICATION_ID
  ,NOTIFICATION_CHANNEL_ID)
   .setChannelNameResourceId(R.string.Notification_cannel_name)
   .setChannelDescriptionResourceId(R.string.Notification_Description)
   .setMediaDescriptionAdapter(DescriptionAdapter(mediacontroller))
   .setNotificationListener(notifcationlistener).build().apply {
    setSmallIcon(R.drawable.ic_music_foreground)
    setMediaSessionToken(sessiontoken)
   }
 }
 fun shownotification(player:Player){
  notificationManager.setPlayer(player)
 }

 inner class DescriptionAdapter(var mediaControllerCompat: MediaControllerCompat):PlayerNotificationManager.MediaDescriptionAdapter{
  override fun getCurrentContentTitle(player: Player): CharSequence {
   return mediaControllerCompat.metadata.description.title.toString()
  }

  override fun createCurrentContentIntent(player: Player): PendingIntent? {
  return mediaControllerCompat.sessionActivity
  }

  override fun getCurrentContentText(player: Player): CharSequence? {
return mediaControllerCompat.metadata.description.subtitle.toString()
  }

  override fun getCurrentLargeIcon(
   player: Player,
   callback: PlayerNotificationManager.BitmapCallback
  ): Bitmap? {
   Glide.with(context).asBitmap().load(mediaControllerCompat.metadata.description.iconUri).into(object:CustomTarget<Bitmap>(){
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
     callback.onBitmap(resource)
    }

    override fun onLoadCleared(placeholder: Drawable?) {

    }

   })
   return null
  }

 }
}