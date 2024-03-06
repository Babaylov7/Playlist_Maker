package com.example.playlistmaker.di

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.db.impl.PlayListRepositoryImpl
import com.example.playlistmaker.data.player.network.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.local.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.network.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SavedSettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.PlayListRepository
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.settings.SavedSettingsRepository
import com.example.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<TrackRepository> {
        TrackRepositoryImpl(
            get(named(NETWORK_CLIENT)),
            get(named(APP_DATABASE))
        )
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            get(named(SEARCH_HISTORY))
        )
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(
            get(named(MEDIA_PLAYER)),
            get()
        )
    }

    factory<SavedSettingsRepository> {
        SavedSettingsRepositoryImpl(
            get(named(SETTINGS_PREFERENCES))
        )
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

    factory { TrackDbConvertor() }
    factory { MediaPlayerStatus.STATE_DEFAULT }

    //Database
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            get(named(APP_DATABASE)),
            get(named(TRACK_DB_CONVERTOR))
        )
    }

    single<PlayListRepository>{
        PlayListRepositoryImpl(
            get(named(APP_DATABASE)),
            get(named(PLAYLIST_DB_CONVERTOR))
        )
    }

}