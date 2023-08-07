package com.example.spotifyclone.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.spotifyclone.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object constants {

    @Singleton
    @Provides
    fun getimage(@ApplicationContext     context:Context)=
        Glide.with(context).setDefaultRequestOptions(RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.DATA))

/* @Provides
@Singleton
fun getmusicserviceconnection(musicServiceConnection: MusicServiceConnection):MusicServiceConnection{
    return  musicServiceConnection
}
*/


}

