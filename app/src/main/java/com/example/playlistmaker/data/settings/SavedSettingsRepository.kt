package com.example.playlistmaker.data.settings

interface SavedSettingsRepository {

    fun putNightModeSettings(mode: Boolean)
    fun switchTheme(darkThemeEnabled: Boolean)
    fun getNightModeSettings(): Boolean
}