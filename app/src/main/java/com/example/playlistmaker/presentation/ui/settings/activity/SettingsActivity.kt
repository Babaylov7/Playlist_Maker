package com.example.playlistmaker.presentation.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel

class SettingsActivity : ComponentActivity() {
    private lateinit var binding: SettingsActivityBinding
    private lateinit var viewModel: SettingsViewModel
    private var savedSettingsInteractorImpl = Creator.provideSavedSettingsInteractor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]


    val buttonBack = findViewById<ImageView>(R.id.button_back)
    val shareApplication = findViewById<LinearLayout>(R.id.share_application)
    val writeToSupport = findViewById<LinearLayout>(R.id.write_to_support)
    val termsOfUse = findViewById<LinearLayout>(R.id.terms_of_use)
    val darkThemeSwitch = findViewById<SwitchCompat>(R.id.dark_theme_switch)

        darkThemeSwitch.setChecked(
            savedSettingsInteractorImpl.getNightModeSettings()
        )          //выставляет значение Switch по значению из настроек

        buttonBack.setOnClickListener {
            finish()
        }

        shareApplication.setOnClickListener {
            shareApplication()
        }

        writeToSupport.setOnClickListener {
            writeToSupport()
        }

        termsOfUse.setOnClickListener {
            termsOfUse()
        }

        darkThemeSwitch.setOnCheckedChangeListener { switcher, checked ->
            viewModel.putNightModeSettings(checked)
            viewModel.switchTheme(checked)
        }

    }

    private fun shareApplication() {
        val link = getString(R.string.share_app_link)
        val tupe = getString(R.string.share_app_type)
        val message = getString(R.string.share_app_message)
        startActivity(viewModel.getShareApplicationIntent(link, tupe, message))
    }

    private fun writeToSupport() {
        val message = getString(R.string.write_to_supp_message)
        val theme = getString(R.string.write_to_supp_theme)
        val mailTo = getString(R.string.write_to_supp_mailto)
        val email = getString(R.string.write_to_supp_email)
        startActivity(viewModel.getWriteToSupportIntent(message, theme, mailTo, email))
    }

    private fun termsOfUse() {
        val link = getString(R.string.terms_of_link)
        startActivity(viewModel.getTermsOfUseIntent(link))
    }
}