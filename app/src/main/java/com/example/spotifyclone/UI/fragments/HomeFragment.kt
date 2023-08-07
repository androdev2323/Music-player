package com.example.spotifyclone.UI.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.spotifyclone.R
import com.example.spotifyclone.UI.viewmodel.MainViewmodel
import com.example.spotifyclone.adapter.songlistadapter
import com.example.spotifyclone.exoplayer.MusicServiceConnection
import com.example.spotifyclone.other.state
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject
@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {

    lateinit var viewmodel: MainViewmodel

    @Inject
  lateinit var glide: RequestManager

  lateinit var songadapter:songlistadapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songadapter=songlistadapter(glide)
        viewmodel = ViewModelProvider(requireActivity()).get(MainViewmodel::class.java)
        (viewmodel.networkerror.observe(viewLifecycleOwner, Observer { data ->
            Log.d("MyActivity", "LiveData value ${data.getcontentifnothandeled()}")
        }))

        setuprecyleview()
        subscribeobservers()
        songadapter.setonItemclickistener {
            viewmodel.playortogglesong(it)
        }
    }

    private fun setuprecyleview() =
        rvAllSongs.apply {

            layoutManager = LinearLayoutManager(requireContext())
            adapter = songadapter
        }


    private fun subscribeobservers() {
        viewmodel.mediaitem.observe(viewLifecycleOwner) { result ->
            when (result.state) {
                state.state_loading -> allSongsProgressBar.isVisible = true
                state.state_success -> {


                    allSongsProgressBar.isVisible = false
                    result.data?.let {
                        songadapter.songs = it
                    }

                }
                state.state_error -> {
                    allSongsProgressBar.isVisible = false
                    Toast.makeText(context, result.error?.toString(), Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}