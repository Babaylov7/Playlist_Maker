package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class SettingsSharedPreferences : Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(NIGHT_MODE_ON, false)
        switchTheme(darkTheme)
    }

    fun putNightMomeSettings(mode: Boolean) {
        sharedPrefs.edit()
            .putBoolean(NIGHT_MODE_ON, mode)
            .apply()
    }

    fun getNightMomeSettings(): Boolean {
        return sharedPrefs.getBoolean(NIGHT_MODE_ON, false)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val SETTINGS_PREFERENCES = "settings_preferences"
        private const val NIGHT_MODE_ON = "night_mode_on"
    }

}