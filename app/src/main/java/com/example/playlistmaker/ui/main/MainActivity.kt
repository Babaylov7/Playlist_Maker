package com.example.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.app.AppSharedPreferences
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.local.SavedSettingsRepositoryImpl
import com.example.playlistmaker.domain.impl.SavedSettingsInteractorImpl
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var savedSettingsInteractorImpl: SavedSettingsInteractorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        savedSettingsInteractorImpl =
            SavedSettingsInteractorImpl(SavedSettingsRepositoryImpl(applicationContext as AppSharedPreferences))
        savedSettingsInteractorImpl.switchTheme(savedSettingsInteractorImpl.getNightModeSettings())

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonLibrary = findViewById<Button>(R.id.button_library)
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonLibrary.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }
}