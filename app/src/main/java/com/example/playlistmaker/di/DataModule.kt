package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.converters.PlayListDbConvertor
import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.network.ItunesApi
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesApi> {
        Retrofit.Builder().baseUrl(ITUNES_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(ItunesApi::class.java)
    }

    single(named(SEARCH_HISTORY)) {
        androidContext().getSharedPreferences(
            SEARCH_HISTORY, Context.MODE_PRIVATE
        )
    }

    single(named(SETTINGS_PREFERENCES)) {
        androidContext().getSharedPreferences(
            SETTINGS_PREFERENCES, Context.MODE_PRIVATE
        )
    }

    factory(named(MEDIA_PLAYER)) {
        MediaPlayer()
    }

    factory<NetworkClient>(named(NETWORK_CLIENT)) {
        RetrofitNetworkClient(get())
    }

    //Database
    single(named(APP_DATABASE)) {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    factory(named(TRACK_DB_CONVERTOR)) { TrackDbConvertor() }

    factory(named(PLAYLIST_DB_CONVERTOR)) { PlayListDbConvertor() }

}



