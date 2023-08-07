package com.example.spotifyclone.exoplayer
import android.media.MediaDataSource
import android.media.MediaMetadata
import android.media.MediaMetadata.*
import android.media.browse.MediaBrowser
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.core.net.toUri
import com.example.spotifyclone.data.remote.Musicdatabase
import com.example.spotifyclone.exoplayer.STATE
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicsource @Inject constructor(private val musicdatabase: Musicdatabase) {
    var metasong= emptyList<MediaMetadataCompat>()
 suspend fun fetchmetadata()= withContext(Dispatchers.IO){

     state=STATE.STATE_INTILIAZING

     val songs=musicdatabase.getlist()
     metasong=songs.map {
        song->
        MediaMetadataCompat.Builder()
            .putString(METADATA_KEY_ARTIST,song.subtitle)
            .putString(METADATA_KEY_MEDIA_ID,song.mediaid)
            .putString(METADATA_KEY_TITLE,song.title)
            .putString(METADATA_KEY_DISPLAY_TITLE,song.title)
            .putString(METADATA_KEY_DISPLAY_ICON_URI,song.imageurl)
            .putString(METADATA_KEY_MEDIA_URI,song.songurl)
            .putString(METADATA_KEY_ALBUM_ART_URI,song.imageurl)
            .putString(METADATA_KEY_DISPLAY_SUBTITLE,song.subtitle)
            .putString(METADATA_KEY_DISPLAY_DESCRIPTION,song.subtitle)
            .build()
    }

     withContext(Dispatchers.Main) {
         state = STATE.STATE_INTIALIZED
     }
 }
    fun asMediasource(dataSource: DefaultDataSourceFactory) :ConcatenatingMediaSource{
        var concatenatingMediaSource=ConcatenatingMediaSource()
        metasong.forEach(){
                song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSource)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI).toUri()))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return  concatenatingMediaSource
    }
    fun asmediaitem(): MutableList<MediaBrowserCompat.MediaItem> {


        return metasong.map { songs ->
            val desc = MediaDescriptionCompat.Builder()
                .setMediaUri(songs.getString(METADATA_KEY_MEDIA_URI).toUri())
                .setTitle(songs.description.title.toString())
                .setSubtitle(songs.description.subtitle)
                .setMediaId(songs.description.mediaId)
                .setIconUri(songs.description.iconUri)
                .build()


            MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)

        }.toMutableList();

    }








    private val onreadylistener= mutableListOf<(Boolean)-> Unit >()
       var  state:STATE=STATE.STATE_CREATED
    set(value) {
        if(value==STATE.STATE_INTIALIZED||value==STATE.STATE_ERROR) {
            synchronized(onreadylistener){
                field = value
                onreadylistener.forEach { listener ->
                    listener(state == STATE.STATE_INTIALIZED)
                }
            }
        }
        else{
            field=value
        }
    }
    fun whenread(action:(Boolean)-> Unit):Boolean{
           if(state==STATE.STATE_CREATED||state==STATE.STATE_INTILIAZING){
               onreadylistener+= action
               return false
           }
        else{
            action(state==STATE.STATE_INTIALIZED)
            return true
           }
    }
}
enum class STATE{
  STATE_CREATED,
    STATE_INTILIAZING,
    STATE_INTIALIZED,
    STATE_ERROR
}