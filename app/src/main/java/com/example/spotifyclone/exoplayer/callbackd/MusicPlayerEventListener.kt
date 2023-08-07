package com.example.spotifyclone.exoplayer.callbackd

import android.widget.Toast
import com.example.spotifyclone.exoplayer.musicservice
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MusicPlayerEventListener(private val musicservice: musicservice): Player.Listener {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if(playbackState==Player.STATE_READY && !playWhenReady){

            musicservice.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicservice, "An unknown error occured", Toast.LENGTH_LONG).show()
    }
}