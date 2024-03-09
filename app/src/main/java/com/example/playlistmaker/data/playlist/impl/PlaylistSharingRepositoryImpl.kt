package com.example.playlistmaker.data.playlist.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlaylistSharingRepository

class PlaylistSharingRepositoryImpl(private val context: Context): PlaylistSharingRepository {
    override fun shareTrackList(playlistInfo: String) {
        val type = context.getString(R.string.share_app_type)
        val intentLink = Intent(Intent.ACTION_SEND)
        intentLink.type = type
        intentLink.putExtra(Intent.EXTRA_TEXT, playlistInfo)
        val intentMessage = Intent.createChooser(intentLink, "Поделиться плейлистом")
        intentMessage.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intentMessage)
    }


     fun shareApplication() {
        val link = context.getString(R.string.share_app_link)           //https://practicum.yandex.ru/profile/android-developer/
        val type = context.getString(R.string.share_app_type)           //ext/plain
        val message = context.getString(R.string.share_app_message)     //Поделиться приложением
        val intentLink = Intent(Intent.ACTION_SEND)
        intentLink.type = type
        intentLink.putExtra(Intent.EXTRA_TEXT, link)
        val intentMessage = Intent.createChooser(intentLink, message)
        intentMessage.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intentMessage)
    }
}