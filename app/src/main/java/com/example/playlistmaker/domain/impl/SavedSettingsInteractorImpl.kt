package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.settings.SavedSettingsInteractor
import com.example.playlistmaker.domain.api.settings.SavedSettingsRepository

class SavedSettingsInteractorImpl(private val savedSettingsRepository: SavedSettingsRepository) :
    SavedSettingsInteractor {
    override fun putNightModeSettings(mode: Boolean) {
        savedSettingsRepository.putNightModeSettings(mode)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        savedSettingsRepository.switchTheme(darkThemeEnabled)
    }

    override fun getNightModeSettings(): Boolean {
        return savedSettingsRepository.getNightModeSettings()
    }
}