package com.example.playlistmaker.presentation.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LibraryFavoriteFragmentBinding
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryFavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFavoriteFragment: BindingFragment<LibraryFavoriteFragmentBinding>() {

    private val viewModel by viewModel<LibraryFavoriteFragmentViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LibraryFavoriteFragmentBinding {
        return LibraryFavoriteFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showErrorImage()
    }

    private fun showErrorImage() {

        if (requireContext().isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_dark)
                .into(binding.ivMessageImage)
        } else {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_light)
                .into(binding.ivMessageImage)
        }
    }

    companion object {
        fun newInstance() = LibraryFavoriteFragment()
    }

}