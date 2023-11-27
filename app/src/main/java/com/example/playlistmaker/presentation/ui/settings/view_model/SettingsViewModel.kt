package com.example.playlistmaker.presentation.ui.settings.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SavedSettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor


class SettingsViewModel(
    val sharingInteractorImpl: SharingInteractor,
    val savedSettingsInteractorImpl: SavedSettingsInteractor
) : ViewModel() {

    private val _nightMode = MutableLiveData(getNightModeSettings())
    val nightMode: LiveData<Boolean> = _nightMode

    private fun getNightModeSettings() : Boolean {
        return savedSettingsInteractorImpl.getNightModeSettings()
    }

    fun shareApplication() {
        sharingInteractorImpl.shareApplication()
    }
    fun writeToSupport() {
        sharingInteractorImpl.writeToSupport()
    }
    fun termsOfUse(){
        sharingInteractorImpl.termsOfUse()
    }

    fun putNightModeSettings(checked: Boolean){
        savedSettingsInteractorImpl.putNightModeSettings(checked)
    }

    fun switchTheme(checked: Boolean){
        savedSettingsInteractorImpl.switchTheme(checked)
    }

}