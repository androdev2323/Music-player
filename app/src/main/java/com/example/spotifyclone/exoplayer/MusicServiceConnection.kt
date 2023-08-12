package com.example.spotifyclone.exoplayer

import android.content.ComponentName
import android.content.Context
import android.media.browse.MediaBrowser
import android.media.session.MediaController
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.spotifyclone.constants.NETWORK_ERROR
import com.example.spotifyclone.other.Event
import com.example.spotifyclone.other.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicServiceConnection @Inject constructor (@ApplicationContext Context:Context) {
    private val _isconected = MutableLiveData<Event<Resource<Boolean>>>()
    val isconectted = _isconected
    lateinit var mediaController: MediaControllerCompat

    private val _networkerror = MutableLiveData<Event<Resource<Boolean>>>()
    val networkerror = _networkerror
    private val _playbackstate = MutableLiveData<PlaybackStateCompat?>()
    val playbackstate = _playbackstate
    private val _currplayinsong = MutableLiveData<MediaMetadataCompat?>()
    val currplayinsong = _currplayinsong
    private var mediabrowserconnectioncallback = MediaBrowserConnectionCallback(Context)
    val mediabrowser = MediaBrowserCompat(
        Context,
        ComponentName(Context, musicservice::class.java),
        mediabrowserconnectioncallback,
        null
    ).apply {
        connect()
    }



    val transportcontrols:MediaControllerCompat.TransportControls
    get()=mediaController.transportControls
    fun subscribe(parentid:String,callback:MediaBrowserCompat.SubscriptionCallback){

            mediabrowser.subscribe(parentid,callback)



    }

    fun unsubscribe(parentid: String,callback: SubscriptionCallback){
        mediabrowser.unsubscribe(parentid,callback)
    }


    private inner class MediaControllercallback:MediaControllerCompat.Callback()
    {
        override fun onSessionEvent(event: String?, extras: Bundle?) {
                     when(event){
                         NETWORK_ERROR->networkerror.postValue(Event(
                             Resource.error(null,"Couldn't connect to the server. Please check your internet connection.")
                         ))
                     }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playbackstate.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            _currplayinsong.postValue(metadata)
        }
    }
    private inner class MediaBrowserConnectionCallback(private val Context:Context):MediaBrowserCompat.ConnectionCallback(){


        override fun onConnected() {
            mediaController=MediaControllerCompat(Context,mediabrowser.sessionToken).apply {
                registerCallback(MediaControllercallback())

            }



           _isconected.postValue(Event(Resource.success(true)))

        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            _isconected.postValue(Event(Resource.error(false,"The connection was suspended")))
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            _isconected.postValue(Event(Resource.error(false,"We failled to connect to media browser")))
        }

    }


}