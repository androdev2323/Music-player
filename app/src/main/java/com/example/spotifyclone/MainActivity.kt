package com.example.spotifyclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.spotifyclone.UI.viewmodel.MainViewmodel
import com.example.spotifyclone.adapter.songlistadapter
import com.example.spotifyclone.adapter.swipesongadapter
import com.example.spotifyclone.exoplayer.toSong
import com.example.spotifyclone.other.state
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewmodel:MainViewmodel by viewModels()
    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var swipesongadapter: swipesongadapter

    private var currplayingsong:Songs?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe()
        vpSong.adapter=swipesongadapter
    }

    private fun switchviewpager(song:Songs){
        val newindex=swipesongadapter.songs.indexOf(song)
        if(newindex!=-1){
            vpSong.currentItem=newindex
            currplayingsong=song
        }
    }

    private  fun observe(){
        mainViewmodel.mediaitem.observe(this){
            it?.let {result->
                when(result.state){
                    state.state_loading -> Unit
                    state.state_success ->{
                      result.data?.let {
                          swipesongadapter.songs=it
                          if(!it.isEmpty()){
                              glide.load((currplayingsong?:it[0]).imageurl).into(ivCurSongImage)
                          }
                          switchviewpager(currplayingsong?:return@observe)

                      }
                    }
                    state.state_error -> Unit
                }
            }
        }
        mainViewmodel.currplayingsong.observe(this){
            if(it == null) return@observe

            currplayingsong = it.toSong()
            glide.load(currplayingsong?.imageurl).into(ivCurSongImage)
            switchviewpager(currplayingsong ?: return@observe)
        }
    }
}