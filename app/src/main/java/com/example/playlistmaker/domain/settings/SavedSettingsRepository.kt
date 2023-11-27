package com.example.playlistmaker.domain.settings

interface SavedSettingsRepository {

    fun putNightModeSettings(mode: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getNightModeSettings(): Boolean
}