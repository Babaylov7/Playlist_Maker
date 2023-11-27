package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingInteractorImpl(private val repository: SharingRepository): SharingInteractor {
    override fun shareApplication() {
        repository.shareApplication()
    }

    override fun writeToSupport() {
        repository.writeToSupport()
    }

    override fun termsOfUse() {
        repository.termsOfUse()
    }


}