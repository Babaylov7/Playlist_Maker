package com.example.playlistmaker.domain.api.settings

interface SavedSettingsInteractor {
    fun putNightModeSettings(mode: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getNightModeSettings(): Boolean
}