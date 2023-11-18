package com.example.playlistmaker.presentation.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsViewModelFactory (val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            context = context
        ) as T
    }
}