package com.example.playlistmaker.domain.sharing

interface SharingRepository {
    fun shareApplication()

    fun writeToSupport()

    fun termsOfUse()
}