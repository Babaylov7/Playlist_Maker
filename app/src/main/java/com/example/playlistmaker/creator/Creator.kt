package com.example.playlistmaker.creator

import com.example.playlistmaker.app.AppSharedPreferences
import com.example.playlistmaker.data.settings.impl.SavedSettingsRepositoryImpl
import com.example.playlistmaker.data.search.local.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.player.network.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.impl.TrackRepositoryImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.data.search.local.SearchHistoryRepository
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.data.player.network.MediaPlayerRepository
import com.example.playlistmaker.domain.settings.SavedSettingsInteractor
import com.example.playlistmaker.data.settings.SavedSettingsRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.data.search.network.TrackRepository
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.settings.impl.SavedSettingsInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl

object Creator {

    private lateinit var app: AppSharedPreferences

    fun initApplication(application: AppSharedPreferences) {
        app = application
    }

    fun provideTrackInteractor(): TrackInteractor {                         //Поиск трека
        return TrackInteractorImpl(getTrackRepository())
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {         //Получение истории поиска
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(app)
    }

    fun provideSavedSettingsInteractor(): SavedSettingsInteractor {         //Сохранение настроек
        return SavedSettingsInteractorImpl(getSavedSettingsRepository())
    }

    private fun getSavedSettingsRepository(): SavedSettingsRepository {
        return SavedSettingsRepositoryImpl(app)
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {             //Работа с медиаплеером
        return MediaPlayerInteractorImpl(getMediaPlayerRepisitory())
    }

    private fun getMediaPlayerRepisitory(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }
}