package com.example.playlistmaker.presentation.ui.library.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LibraryPlaylistsFragmentBinding
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryPlaylistsFragmentViewModel
import com.example.playlistmaker.presentation.ui.main.MainActivity
import com.example.playlistmaker.presentation.ui.new_playlist.NewPlaylistFragment
import com.example.playlistmaker.presentation.ui.search.fragment.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistsFragment: BindingFragment<LibraryPlaylistsFragmentBinding>() {

    private val viewModel = viewModel<LibraryPlaylistsFragmentViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LibraryPlaylistsFragmentBinding {
        return LibraryPlaylistsFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showErrorImage()

        binding.bNewPlaylist.setOnClickListener {
            (activity as? MainActivity)?.hideNavBar()

            findNavController().navigate( R.id.action_libraryFragment_to_newPlaylistFragment)

        }
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

    companion object{
        fun newInstance() = LibraryPlaylistsFragment()
    }
}