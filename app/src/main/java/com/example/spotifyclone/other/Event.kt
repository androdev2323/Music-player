package com.example.spotifyclone.other

class Event<out T>(private val data:T?){
  var hasbeenhandled=false
    private set

    fun getcontentifnothandeled():T?{
 return if(hasbeenhandled){
     null
 }
        else{
            hasbeenhandled=true
       data
 }
    }


}