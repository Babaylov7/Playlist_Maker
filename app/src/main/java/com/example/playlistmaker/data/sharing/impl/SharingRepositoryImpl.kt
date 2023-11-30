package com.example.playlistmaker.data.sharing.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val app: App): SharingRepository {

    override fun shareApplication() {
        val link = app.getString(R.string.share_app_link)
        val type = app.getString(R.string.share_app_type)
        val message = app.getString(R.string.share_app_message)
        val intentLink = Intent(Intent.ACTION_SEND)
        intentLink.type = type
        intentLink.putExtra(Intent.EXTRA_TEXT, link)
        val intentMessage = Intent.createChooser(intentLink, message)
        intentMessage.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        app.startActivity(intentMessage)
    }

    override fun writeToSupport() {
        val message = app.getString(R.string.write_to_supp_message)
        val theme = app.getString(R.string.write_to_supp_theme)
        val mailTo = app.getString(R.string.write_to_supp_mailto)
        val email = app.getString(R.string.write_to_supp_email)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(mailTo)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.putExtra(Intent.EXTRA_SUBJECT, theme)
        app.startActivity(intent)
    }

    override fun termsOfUse() {
        val link = app.getString(R.string.terms_of_link)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        app.startActivity(intent)
    }
}