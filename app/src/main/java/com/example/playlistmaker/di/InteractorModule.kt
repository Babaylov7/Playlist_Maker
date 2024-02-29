package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.db.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.domain.db.impl.PlayListInteractorImpl
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.SavedSettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SavedSettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<SavedSettingsInteractor> {
        SavedSettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    //Database
    single<FavoriteTracksInteractor>{
        FavoriteTracksInteractorImpl(get())
    }

    single<PlayListInteractor>{
        PlayListInteractorImpl(get())
    }
}