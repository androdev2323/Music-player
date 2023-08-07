package com.example.spotifyclone.exoplayer
import android.support.v4.media.session.PlaybackStateCompat

    inline val PlaybackStateCompat.isprepared
    get()= state==PlaybackStateCompat.STATE_BUFFERING||
           state ==PlaybackStateCompat.STATE_PAUSED||
            state== PlaybackStateCompat.STATE_PLAYING


inline val PlaybackStateCompat.isplaying
get()=state==PlaybackStateCompat.STATE_BUFFERING||
        state==PlaybackStateCompat.STATE_PLAYING

inline val PlaybackStateCompat.iSPlayEnabled
get()= actions and PlaybackStateCompat.ACTION_PLAY !=0L || (actions and PlaybackStateCompat.ACTION_PLAY != 0L && state==PlaybackStateCompat.STATE_PAUSED )