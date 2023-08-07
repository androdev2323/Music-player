package com.example.spotifyclone.data.remote

import com.example.spotifyclone.Songs
import com.example.spotifyclone.constants.COLLECTION_NAME
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Musicdatabase {
    private var instance=FirebaseFirestore.getInstance()
    private var songcollection=instance.collection(COLLECTION_NAME)

    suspend fun getlist():List<Songs>{
     return try{
         songcollection.get().await().toObjects(Songs::class.java)
     }
     catch (e:java.lang.Exception){
         return emptyList()
     }
    }
}