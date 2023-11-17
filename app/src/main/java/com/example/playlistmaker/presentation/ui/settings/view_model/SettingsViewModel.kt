package com.example.playlistmaker.presentation.ui.settings.view_model

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator


class SettingsViewModel(
) : ViewModel() {
    private var savedSettingsInteractorImpl = Creator.provideSavedSettingsInteractor()

    var liveData = MutableLiveData(getNightModeSettings())

    private fun getNightModeSettings() : Boolean {
        return savedSettingsInteractorImpl.getNightModeSettings()
    }

    fun getShareApplicationIntent(shareAppLink: String, shareAppType: String, shareAppMessage: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = shareAppType
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        return Intent.createChooser(intent, shareAppMessage)
    }
    fun getWriteToSupportIntent(message: String, theme: String, mailTo: String, email: String): Intent {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(mailTo)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.putExtra(Intent.EXTRA_SUBJECT, theme)
        return intent
    }
    fun getTermsOfUseIntent(link: String): Intent{
        return Intent(Intent.ACTION_VIEW, Uri.parse(link))
    }

    fun putNightModeSettings(checked: Boolean){
        savedSettingsInteractorImpl.putNightModeSettings(checked)
    }

    fun switchTheme(checked: Boolean){
        savedSettingsInteractorImpl.switchTheme(checked)
    }

}