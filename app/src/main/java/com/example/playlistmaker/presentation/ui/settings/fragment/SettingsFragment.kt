package com.example.playlistmaker.presentation.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<SettingsFragmentBinding>() {

    private val viewModel by viewModel<SettingsViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingsFragmentBinding {
        return SettingsFragmentBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nightMode: Boolean? = viewModel.nightMode.value

        binding.darkThemeSwitch.setChecked(
            nightMode ?: false
        )

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