package com.example.spotifyclone.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.spotifyclone.R
import com.example.spotifyclone.Songs
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class songlistadapter @Inject constructor(private val glide:RequestManager): BaseSongadapter(R.layout.list_item) {


   override val differ=AsyncListDiffer(this,diffcallback)

    override fun onBindViewHolder(holder: songviewholder, position: Int) {
       val song=songs[position]
       holder.itemView.apply{
           tvPrimary.text=song.title.toString()
           tvSecondary.text=song.subtitle
           Log.d("ui","we have reached here")
           glide.load(song.imageurl).into(ivItemImage)
          setOnClickListener() {
               onItemClickListener?.let {
                  it(song)
               }
           }
       }

    }


}