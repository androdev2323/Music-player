package com.example.spotifyclone.exoplayer.callbackd

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.spotifyclone.exoplayer.FirebaseMusicsource
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector

class MusicPlayerPlaybackPrepare(val firebaseMusicsource: FirebaseMusicsource,private val playerprepared: (MediaMetadataCompat?)-> Unit):MediaSessionConnector.PlaybackPreparer {
    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
firebaseMusicsource.whenread(){
    val itemtolay=firebaseMusicsource.metasong.find {
        mediaId==it.description.mediaId
    }
    playerprepared(itemtolay)
}
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
        TODO("Not yet implemented")
    }
}