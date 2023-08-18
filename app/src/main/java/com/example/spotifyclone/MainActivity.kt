package com.example.spotifyclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.spotifyclone.UI.viewmodel.MainViewmodel
import com.example.spotifyclone.adapter.songlistadapter
import com.example.spotifyclone.adapter.swipesongadapter
import com.example.spotifyclone.exoplayer.isplaying
import com.example.spotifyclone.exoplayer.toSong
import com.example.spotifyclone.other.state
import com.google.android.material.snackbar.Snackbar
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
    private var playbackState: PlaybackStateCompat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe()
        vpSong.adapter=swipesongadapter
        vpSong.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(playbackState?.isplaying==true){
                   mainViewmodel.playortogglesong(swipesongadapter.songs[position])
                }
                else{
                    currplayingsong=swipesongadapter.songs[position]
                }
            }
        })
        ivPlayPause.setOnClickListener(){
            currplayingsong?.let {
                mainViewmodel.playortogglesong(it,true)
            }
        }
        swipesongadapter.setItemclickistener {
            navHostFragment.findNavController().navigate(R.id.globalactionTosongfragment)
        }

           navHostFragment.findNavController().addOnDestinationChangedListener{
                   _,destination,_ ->
               when(destination.id){
                   R.id.songFragment-> hidebottombar()
                   R.id.homeFragment->showbottombar()
                   else -> showbottombar()
               }
           }

    }

    fun hidebottombar(){
        ivCurSongImage.isVisible=false
        ivPlayPause.isVisible=false
        vpSong.isVisible=false

    }
    fun showbottombar(){
        ivCurSongImage.isVisible=true
        ivPlayPause.isVisible=true
        vpSong.isVisible=true

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

        mainViewmodel.playbackstate.observe(this){
            playbackState=it
            ivPlayPause.setImageResource(if(playbackState?.isplaying==true)R.drawable.ic_pause else R.drawable.ic_play)
        }
        mainViewmodel.isconnectd.observe(this){
            it?.getcontentifnothandeled()?.let {result->
                when(result.state){
                    state.state_error-> Snackbar.make(rootLayout,result.error?:"an unkown error ocurred",
                        Snackbar.LENGTH_LONG).show()

                    else -> {Unit}
                }

            }
        }
        mainViewmodel.networkerror.observe(this){
            it?.getcontentifnothandeled()?.let {
                result->
               when(result.state){
                   state.state_error-> Snackbar.make(rootLayout,result.error?:"an unkown error ocurred",
                   Snackbar.LENGTH_LONG).show()
                   else -> {Unit}
               }
            }
        }

    }
}