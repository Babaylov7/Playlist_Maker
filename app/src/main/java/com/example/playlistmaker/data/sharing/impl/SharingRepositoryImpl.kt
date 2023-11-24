package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {
    override fun shareApplication() {
        val link = context.getString(R.string.share_app_link)
        val tupe = context.getString(R.string.share_app_type)
        val message = context.getString(R.string.share_app_message)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = tupe
        intent.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(Intent.createChooser(intent, message))
    }

    override fun writeToSupport() {
        val message = context.getString(R.string.write_to_supp_message)
        val theme = context.getString(R.string.write_to_supp_theme)
        val mailTo = context.getString(R.string.write_to_supp_mailto)
        val email = context.getString(R.string.write_to_supp_email)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(mailTo)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.putExtra(Intent.EXTRA_SUBJECT, theme)
        context.startActivity(intent)
    }

    override fun termsOfUse() {
        val link = context.getString(R.string.terms_of_link)
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}