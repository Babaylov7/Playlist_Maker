package com.example.playlistmaker

import android.R.attr.text
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

const val SETTINGS_PREFERENCES = "settings_preferences"
const val NIGHT_MODE_ON = "night_mode_on"

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val sharedPrefs = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val sareApplicationButton = findViewById<LinearLayout>(R.id.sare_application)
        val writeToSupportButton = findViewById<LinearLayout>(R.id.write_to_support)
        val termsOfUseButton = findViewById<LinearLayout>(R.id.terms_of_use)
        val darkThemeSwitch = findViewById<SwitchCompat>(R.id.dark_theme_switch)

        darkThemeSwitch.setChecked(sharedPrefs.getBoolean(NIGHT_MODE_ON, true))

        buttonBack.setOnClickListener {
            finish()
        }

        sareApplicationButton.setOnClickListener {
            val link = getString(R.string.share_app_link)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = getString(R.string.share_app_type)
            intent.putExtra(Intent.EXTRA_TEXT, link) // текст отправки
            startActivity(Intent.createChooser(intent, getString(R.string.share_app_message)))
        }

        writeToSupportButton.setOnClickListener {
            val message = getString(R.string.write_to_supp_message)
            val theme = getString(R.string.write_to_supp_theme)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(getString(R.string.write_to_supp_mailto))
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.write_to_supp_email)))
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.putExtra(Intent.EXTRA_SUBJECT, theme)
            startActivity(intent)
        }

        termsOfUseButton.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_of_link)))
            startActivity(intent)
        }

        darkThemeSwitch.setOnCheckedChangeListener { switcher, checked ->
            sharedPrefs.edit()
                .putBoolean(NIGHT_MODE_ON, darkThemeSwitch.isChecked())
                .apply()

            (applicationContext as App).switchTheme(checked)
        }

    }
}