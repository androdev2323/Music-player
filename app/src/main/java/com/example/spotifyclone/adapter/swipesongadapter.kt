package com.example.spotifyclone.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.example.spotifyclone.R
import com.example.spotifyclone.Songs
import kotlinx.android.synthetic.main.list_item.view.*

class swipesongadapter:BaseSongadapter(R.layout.swipe_item) {
    override val differ= AsyncListDiffer(this,diffcallback)


    override fun onBindViewHolder(holder: songviewholder, position: Int) {
        val song=songs[position]
        holder.itemView.apply {
            val text = "${song.title} - ${song.subtitle}"
            tvPrimary.text=text
            setOnClickListener(){
                onItemClickListener?.let {
                    it(song)
                }
            }
        }
    }
}