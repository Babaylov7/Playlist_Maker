package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.db.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.domain.db.impl.PlayListInteractorImpl
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.playlist.PlaylistSharingInteractor
import com.example.playlistmaker.domain.playlist.impl.PlaylistSharingInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.SavedSettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SavedSettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackInteractor>(named(TRACK_INTERACTOR)) {
        TrackInteractorImpl(get())
    }

    single<SearchHistoryInteractor>(named(SEARCH_HISTORY_INTERACTOR)) {
        SearchHistoryInteractorImpl(get())
    }

    factory<MediaPlayerInteractor>(named(MEDIA_PLAYER_INTERACTOR)) {
        MediaPlayerInteractorImpl(get())
    }

    factory<SavedSettingsInteractor>(named(SAVED_SETTINGS_INTERACTOR)) {
        SavedSettingsInteractorImpl(get())
    }

    factory<SharingInteractor>(named(SHARING_INTERACTOR)) {
        SharingInteractorImpl(get())
    }

    //Database
    single<FavoriteTracksInteractor>(named(FAVORITE_TRACKS_INTERACTOR)){
        FavoriteTracksInteractorImpl(get())
    }

    single<PlayListInteractor>(named(PLAY_LIST_INTERACTOR)){
        PlayListInteractorImpl(get())
    }

    single<PlaylistSharingInteractor>(named(PLAYLIST_SHARING_INTERACTOR)){
        PlaylistSharingInteractorImpl(get())
    }
}