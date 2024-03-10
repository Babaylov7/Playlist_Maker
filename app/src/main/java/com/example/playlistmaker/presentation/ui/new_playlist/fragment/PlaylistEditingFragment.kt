package com.example.playlistmaker.presentation.ui.new_playlist.fragment

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.presentation.ui.new_playlist.view_model.PlaylistEditingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditingFragment(): NewPlaylistFragment() {

    private val viewModel by viewModel<PlaylistEditingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getInt(PLAYLIST_KEY)
        viewModel.updatePlaylist(playlistId!!)

        viewModel.updatePlaylist(playlistId)

        viewModel.playlist().observe(viewLifecycleOwner){
            writeDataInFragment(it)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                binding.bCreatePlaylist.isEnabled = !p0.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        binding.tietName.addTextChangedListener(simpleTextWatcher)

        binding.bCreatePlaylist.text = getString(R.string.save)
        binding.tvTitle.text = getString(R.string.edit)

        binding.bCreatePlaylist.setOnClickListener {
            viewModel.updatePlaylistInfo(binding.tietName.text.toString(), binding.tietDescription.text.toString())
            findNavController().navigateUp()
        }

        binding.ivButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun writeDataInFragment(playlist: PlayList) {
        binding.tietDescription.setText(playlist.playlistDescription)
        binding.tietName.setText(playlist.playlistName)

        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

        Glide.with(requireContext())
            .load(playlist.playlistImage?.let { playlistImage -> File(filePath, playlistImage) })
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(binding.ivAlbum)
    }

    companion object {
        private const val DIRECTORY = "album_images"
        private const val PLAYLIST_KEY = "playlist"
    }
}