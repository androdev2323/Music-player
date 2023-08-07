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

class songlistadapter @Inject constructor(private val glide:RequestManager):RecyclerView.Adapter<songlistadapter.songlistviewholder>() {

    inner class songlistviewholder(itemview: View) :RecyclerView.ViewHolder(itemview)
    private val diffcallback=object :DiffUtil.ItemCallback<Songs>(){
        override fun areItemsTheSame(oldItem: Songs, newItem: Songs): Boolean {
            return oldItem.mediaid==newItem.mediaid
        }

        override fun areContentsTheSame(oldItem: Songs, newItem: Songs): Boolean {
           return oldItem.hashCode()==newItem.hashCode()
        }

    }
    private val differ=AsyncListDiffer(this,diffcallback)
    var songs:List<Songs>
    get() = differ.currentList
    set(value)=differ.submitList(value)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): songlistviewholder {
        return songlistviewholder(LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false))
    }

    override fun getItemCount(): Int {
       return songs.size
    }

    override fun onBindViewHolder(holder: songlistviewholder, position: Int) {
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

    private var onItemClickListener:((Songs) -> Unit)?=null

   fun setonItemclickistener(listener:(Songs)-> Unit){
        onItemClickListener=listener
    }
}