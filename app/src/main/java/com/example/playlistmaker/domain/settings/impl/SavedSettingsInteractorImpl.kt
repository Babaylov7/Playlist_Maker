package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SavedSettingsInteractor
import com.example.playlistmaker.domain.settings.SavedSettingsRepository

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