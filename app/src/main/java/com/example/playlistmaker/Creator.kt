package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.app.AppSharedPreferences
import com.example.playlistmaker.data.local.SavedSettingsRepositoryImpl
import com.example.playlistmaker.data.local.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.history.SearchHistoryRepository
import com.example.playlistmaker.domain.api.media_player.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.media_player.MediaPlayerRepository
import com.example.playlistmaker.domain.api.settings.SavedSettingsInteractor
import com.example.playlistmaker.domain.api.settings.SavedSettingsRepository
import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SavedSettingsInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private lateinit var app: AppSharedPreferences

    fun initApplication(application: AppSharedPreferences) {
        this.app = application
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