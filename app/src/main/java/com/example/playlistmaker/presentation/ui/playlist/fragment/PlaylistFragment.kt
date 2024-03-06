package com.example.playlistmaker.presentation.ui.playlist.fragment


import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.domain.playlist.PlayList
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.library.PlayListViewHolder
import java.io.File

class PlaylistFragment: BindingFragment<PlaylistFragmentBinding>() {

    private lateinit var playlist: PlayList

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PlaylistFragmentBinding {
        return PlaylistFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = arguments?.getParcelable<Track>(PLAYLIST_KEY) as PlayList
        writeDataInFragment(playlist)
    }


    private fun writeDataInFragment(playlist: PlayList){
        binding.tvPlaylistName.text = playlist.playlistName
        tracksCount.text = playlist.tracksCount.toString() + " " + itemView.context.resources.getQuantityString(
            R.plurals.plurals, playlist.tracksCount)

        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

        Glide.with(requireContext())
            .load(playlist.playlistImage?.let { playlistImage -> File(filePath, playlistImage) })
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(playlistImage)

    }

    companion object{
        private const val PLAYLIST_KEY = "playlist"
        private const val DIRECTORY = "album_images"
    }

}