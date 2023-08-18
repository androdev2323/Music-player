package com.example.spotifyclone.UI.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.spotifyclone.R
import com.example.spotifyclone.Songs
import com.example.spotifyclone.UI.viewmodel.MainViewmodel
import com.example.spotifyclone.UI.viewmodel.SongViewmodel
import com.example.spotifyclone.exoplayer.toSong
import com.example.spotifyclone.other.state
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_song.*
import javax.inject.Inject
@AndroidEntryPoint

class SongFragment():Fragment(R.layout.fragment_song) {

    @Inject
    lateinit var glide:RequestManager
    private lateinit var mainviewmodel:MainViewmodel
    private val songViewModel: SongViewmodel by viewModels()
    private var currplayingsong:Songs?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainviewmodel=ViewModelProvider(requireActivity()).get(MainViewmodel::class.java)
        subscribetoobservers()
    }
    private fun updatetitleandsong(Song:Songs){
        val title="${Song.title}-${Song.subtitle}"
        tvSongName.text=title
        glide.load(Song.imageurl).into(ivSongImage)
    }
    private fun subscribetoobservers(){
       mainviewmodel.mediaitem.observe(viewLifecycleOwner){
            when(it.state) {
                state.state_success -> it.data?.let {
                    if (currplayingsong == null && !it.isEmpty()){
                        currplayingsong=it[0]
                        updatetitleandsong(currplayingsong!!)
                    }
                }
                else->Unit
            }
        }
       mainviewmodel.currplayingsong.observe(viewLifecycleOwner){
          if(it==null) return@observe
            currplayingsong=it.toSong()
            updatetitleandsong(currplayingsong!!)
        }
    }
}