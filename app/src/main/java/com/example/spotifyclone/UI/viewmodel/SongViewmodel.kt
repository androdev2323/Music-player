package com.example.spotifyclone.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.constants.UPDATE_PLAYER_POSITION_INTERVAL
import com.example.spotifyclone.exoplayer.MusicServiceConnection
import com.example.spotifyclone.exoplayer.currentplaybackposition
import com.example.spotifyclone.exoplayer.musicservice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SongViewmodel @Inject constructor(musicServiceConnection: MusicServiceConnection):ViewModel() {
    private val playbackState = musicServiceConnection.playbackstate

    private val _cursongduration = MutableLiveData<Long>()
    val cursongduration = _cursongduration

    private val _curPlayerposition = MutableLiveData<Long?>()
    val curPlayerposition = _curPlayerposition

    init {
 updateplayerposition()
    }

    private fun updateplayerposition() {
        viewModelScope.launch {
            while (true) {
                val pos = playbackState.value?.currentplaybackposition
                if (curPlayerposition.value != pos) {
                    _curPlayerposition.postValue(pos)
                    _cursongduration.postValue(musicservice.curSongDuration)

                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }

    }
}