package com.example.spotifyclone.UI.viewmodel

import android.media.MediaMetadata.METADATA_KEY_MEDIA_ID
import android.media.browse.MediaBrowser.MediaItem
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.*


import com.example.spotifyclone.Songs
import com.example.spotifyclone.adapter.songlistadapter
import com.example.spotifyclone.constants.MEDIA_ROOT_ID
import com.example.spotifyclone.exoplayer.*
import com.example.spotifyclone.other.Resource
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(  private val  musicservice:MusicServiceConnection): ViewModel() {
    private val _mediaitem= MutableLiveData<Resource<List<Songs>>>()
    val mediaitem:LiveData<Resource<List<Songs>>> = _mediaitem

    val isconnectd=musicservice.isconectted
    val networkerror=musicservice.networkerror
    val currplayingsong=musicservice.currplayinsong
    val playbackstate=musicservice.playbackstate
    init {



        _mediaitem.postValue(Resource.loading(null))


        musicservice.subscribe(MEDIA_ROOT_ID,object :MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val item=children.map {
                    Songs(
                        it.description.iconUri.toString(),
                        it.description.mediaId.toString(),
                        it.description.mediaUri.toString(),
                        it.description.subtitle.toString(),
                        it.description.title.toString()
                    )
                }
                Log.d("yo50",item[0].toString())


                _mediaitem.postValue(Resource.success(item))
            }

            override fun onError(parentId: String) {
                super.onError(parentId)

            }

        })

    }
    fun skiptonext(){
        musicservice.transportcontrols.skipToNext()
    }
    fun skipToPreviousSong() {
        musicservice.transportcontrols.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicservice.transportcontrols.seekTo(pos)
    }

    fun playortogglesong(mediaitem:Songs,toggle:Boolean=false){
        val music=playbackstate.value?.isprepared ?: false
        if(music && mediaitem.mediaid == currplayingsong.value?.getString(METADATA_KEY_MEDIA_ID)){
          playbackstate.value?.let { playbackstate->
          when{
             playbackstate.isplaying -> if(toggle) musicservice.transportcontrols.pause()
              playbackstate.iSPlayEnabled-> musicservice.transportcontrols.play()
              else -> Unit
          }

          }
        }
        else{
            musicservice.transportcontrols.playFromMediaId(mediaitem.mediaid,null)
        }


    }

}