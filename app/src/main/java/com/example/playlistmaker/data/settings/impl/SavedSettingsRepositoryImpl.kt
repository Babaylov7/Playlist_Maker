package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.SavedSettingsRepository

class SavedSettingsRepositoryImpl(private val sharedPrefs: SharedPreferences): SavedSettingsRepository {

    private var darkTheme = getNightModeSettings()

    override fun putNightModeSettings(mode: Boolean) {
        sharedPrefs.edit()
            .putBoolean(NIGHT_MODE_ON, mode)
            .apply()
    }

    override fun getNightModeSettings(): Boolean {
        return sharedPrefs.getBoolean(NIGHT_MODE_ON, false)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
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
        private const val NIGHT_MODE_ON = "night_mode_on"
    }
}