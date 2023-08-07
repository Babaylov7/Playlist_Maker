package com.example.playlistmaker

import android.R.attr.text
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val sareApplicationButton = findViewById<LinearLayout>(R.id.sare_application)
        val writeToSupportButton = findViewById<LinearLayout>(R.id.write_to_support)
        val termsOfUseButton = findViewById<LinearLayout>(R.id.terms_of_use)

        buttonBack.setOnClickListener {
            finish()
        }

        sareApplicationButton.setOnClickListener {
            val link = "https://practicum.yandex.ru/profile/android-developer/"
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            //intent.setPackage(packageName) //имя пакета приложения
            intent.putExtra(Intent.EXTRA_TEXT, link) // текст отправки
            startActivity(Intent.createChooser(intent, "Поделиться приложением"))
        }

        writeToSupportButton.setOnClickListener {
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val theme = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("babaylov-ayu@mail.ru"))
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.putExtra(Intent.EXTRA_SUBJECT, theme)
            startActivity(intent)
        }

        termsOfUseButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(intent)
        }

    }
}