package com.example.spotifyclone.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.media.session.MediaController
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.spotifyclone.constants.MEDIA_ROOT_ID
import com.example.spotifyclone.constants.NETWORK_ERROR
import com.example.spotifyclone.exoplayer.callbackd.MusicPlayerEventListener
import com.example.spotifyclone.exoplayer.callbackd.MusicPlayerNotificationListener
import com.example.spotifyclone.exoplayer.callbackd.MusicPlayerPlaybackPrepare
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class musicservice: MediaBrowserServiceCompat() {

    @Inject
    lateinit var datasourcefactory:DefaultDataSourceFactory
    @Inject
    lateinit var exolayer:ExoPlayer
    @Inject
    lateinit var firebaseMusicsource: FirebaseMusicsource

    private val servicejob= Job()
    private val servicescope= CoroutineScope(Dispatchers.Main + servicejob)
    private lateinit var musicplayereventlistener:MusicPlayerEventListener

    private lateinit var mediasession:MediaSessionCompat
    private lateinit var mediasessionconnector:MediaSessionConnector
    private lateinit var musicNotificationManager: MusicNotificationManager


var isforeground=false
    var isplayerintialized=false
    private var currplayingsong:MediaMetadataCompat?=null



    override fun onCreate() {
        super.onCreate()
        servicescope.launch {
            firebaseMusicsource.fetchmetadata()
        }
        var intent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        val muicplaybackpreparer = MusicPlayerPlaybackPrepare(firebaseMusicsource) {
            currplayingsong = it
            prepareplayer(firebaseMusicsource.metasong, currplayingsong, true)
        }
        musicplayereventlistener= MusicPlayerEventListener(this)


        mediasession = MediaSessionCompat(this, "Servicetag").apply {
            setSessionActivity(intent)
            isActive = true
        }
        sessionToken = mediasession.sessionToken
        mediasessionconnector = MediaSessionConnector(mediasession)
        mediasessionconnector.setPlaybackPreparer(muicplaybackpreparer)
        mediasessionconnector.setPlayer(exolayer)
        exolayer.addListener(musicplayereventlistener)

        musicNotificationManager =
            MusicNotificationManager(
                this,
                mediasession.sessionToken,
                MusicPlayerNotificationListener(this),
                {


                })
        musicNotificationManager.shownotification(exolayer)
    }

    private fun prepareplayer(songs:List<MediaMetadataCompat>,itemtoplay:MediaMetadataCompat?,PlayNow:Boolean){
           val cursongindex=if(currplayingsong==null)0 else songs.indexOf(itemtoplay)
            exolayer.prepare(firebaseMusicsource.asMediasource(datasourcefactory))
        exolayer.seekTo(cursongindex,0)
        exolayer.playWhenReady=PlayNow
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exolayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        servicescope.cancel()
        exolayer.removeListener(musicplayereventlistener)
        exolayer.release()

    }
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
       return BrowserRoot(MEDIA_ROOT_ID,null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
       when(parentId){
           MEDIA_ROOT_ID->{
               val resutsend=firebaseMusicsource.whenread {isintialized->
               if(isintialized){
                  result.sendResult(firebaseMusicsource.asmediaitem())
                   Log.d("mediaitem",firebaseMusicsource.asmediaitem()[0].description.mediaDescription.toString())
                   if(!isplayerintialized && !firebaseMusicsource.metasong.isEmpty()){
                       prepareplayer(firebaseMusicsource.metasong,firebaseMusicsource.metasong[0],false)
                       isplayerintialized=true

                   }
               }
                   else{
                       mediasession.sendSessionEvent(NETWORK_ERROR,null)
                   Log.d("listwwewwf","we have send n result")


               }


               }
               if(!resutsend){
                   Log.d("listwwewwf","we have  not send a result")
                   result.detach()
               }
           }
       }

    }
}