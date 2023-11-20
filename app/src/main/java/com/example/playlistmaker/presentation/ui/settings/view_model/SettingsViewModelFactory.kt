package com.example.playlistmaker.presentation.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.settings.SavedSettingsInteractor

class SettingsViewModelFactory (val context: Context, val savedSettingsInteractorImpl: SavedSettingsInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            context = context,
            savedSettingsInteractorImpl = savedSettingsInteractorImpl
        ) as T
    }
}