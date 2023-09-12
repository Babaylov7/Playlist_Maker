package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson

class AppSharedPreferences : Application() {

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

    fun writeSearchHistory(tracks: ArrayList<Track>) {
        val json = createJsonFromTracks(tracks)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    fun readSearchHistory(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null) ?: return ArrayList<Track>()
        return createTracksFromJson(json)
    }


    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String): ArrayList<Track> {
        return Gson().fromJson(json, ArrayList<Track>()::class.java)
    }

    companion object {
        private const val SETTINGS_PREFERENCES = "settings_preferences"
        private const val NIGHT_MODE_ON = "night_mode_on"
        private const val SEARCH_HISTORY = "search_history"
    }

}