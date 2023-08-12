package com.example.spotifyclone.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.spotifyclone.Songs

fun MediaMetadataCompat.toSong(): Songs? {
    return description?.let {
         Songs( it.iconUri.toString()?:"",
                it.mediaId.toString()?:"",
             it.mediaUri.toString()?: "",
             it.subtitle.toString()?:"",
                it.title.toString()?: "")

    }
}