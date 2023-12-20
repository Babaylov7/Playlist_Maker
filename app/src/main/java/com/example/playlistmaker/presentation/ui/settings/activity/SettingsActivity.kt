package com.example.playlistmaker.presentation.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nightMode: Boolean? = viewModel.nightMode.value

        binding.darkThemeSwitch.setChecked(
            nightMode ?: false
        )

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.shareApplication.setOnClickListener {
            viewModel.shareApplication()
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.termsOfUse.setOnClickListener {
            viewModel.termsOfUse()
        }

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.putNightModeSettings(checked)
            viewModel.switchTheme(checked)
        }
    }
}