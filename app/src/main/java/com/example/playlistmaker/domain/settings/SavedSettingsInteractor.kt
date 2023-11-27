package com.example.playlistmaker.domain.settings

interface SavedSettingsInteractor {
    fun putNightModeSettings(mode: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getNightModeSettings(): Boolean
}