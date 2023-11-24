package com.example.playlistmaker.presentation.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SettingsViewModelFactory (var context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractorImpl = Creator.provideSharingInteractor(context),
            savedSettingsInteractorImpl = Creator.provideSavedSettingsInteractor()
        ) as T
    }
}