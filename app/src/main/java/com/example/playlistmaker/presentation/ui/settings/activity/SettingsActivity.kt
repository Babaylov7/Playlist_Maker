package com.example.playlistmaker.presentation.ui.settings.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.SettingsActivityBinding
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModelFactory

class SettingsActivity : ComponentActivity() {
    private lateinit var binding: SettingsActivityBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        val nightMode: Boolean? = viewModel.liveData.value

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