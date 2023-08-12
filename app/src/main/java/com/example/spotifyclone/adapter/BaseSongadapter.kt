package com.example.spotifyclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyclone.Songs

abstract class BaseSongadapter(val layout:Int):RecyclerView.Adapter<BaseSongadapter.songviewholder>() {
    inner class songviewholder(itemview: View):RecyclerView.ViewHolder(itemview)

    protected val diffcallback=object:DiffUtil.ItemCallback<Songs>(){
        override fun areItemsTheSame(oldItem: Songs, newItem: Songs): Boolean {
           return  oldItem.mediaid==newItem.mediaid
        }

        override fun areContentsTheSame(oldItem: Songs, newItem: Songs): Boolean {
          return oldItem.hashCode()==newItem.hashCode()
        }

    }
    protected abstract val differ: AsyncListDiffer<Songs>
    var songs:List<Songs>
        get() {
         return  differ.currentList
        }
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): songviewholder {
        return songviewholder(LayoutInflater.from(parent.context).inflate(layout,parent,false))
    }

    override fun getItemCount(): Int {
    return songs.size
    }
    protected var onItemClickListener:((Songs) -> Unit)?=null

    fun setItemclickistener(listener:(Songs)-> Unit){
        onItemClickListener=listener
    }
}