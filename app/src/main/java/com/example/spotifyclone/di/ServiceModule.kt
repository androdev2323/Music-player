package com.example.spotifyclone.di

import android.content.Context
import com.example.spotifyclone.data.remote.Musicdatabase

import com.google.android.exoplayer2.util.Util

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun getdb():Musicdatabase {
        return Musicdatabase()
    }

    @ServiceScoped
      @Provides
    fun provideautoattribute()= AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).setUsage(
          C.USAGE_MEDIA).build()

     @Provides
     @ServiceScoped
    fun provideExoPlayer(@ApplicationContext context:Context,audioAttributes: AudioAttributes)=ExoPlayer.Builder(context).setAudioAttributes(audioAttributes,true)
         .setHandleAudioBecomingNoisy(true).build()

    @Provides
    @ServiceScoped
    fun providedatasourcefactory(@ApplicationContext context:Context)=DefaultDataSourceFactory(context,Util.getUserAgent(context, "Spotify App"))

}