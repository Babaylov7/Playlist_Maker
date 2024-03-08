package com.example.playlistmaker.presentation.ui.playlist.fragment


import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.playlist.TrackAdapterForPlaylist
import com.example.playlistmaker.presentation.ui.playlist.view_model.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : BindingFragment<PlaylistFragmentBinding>() {

    private val viewModel by viewModel<PlaylistViewModel>()
    private val tracks: ArrayList<Track> = ArrayList<Track>()

    private lateinit var adapter: TrackAdapterForPlaylist

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PlaylistFragmentBinding {
        return PlaylistFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TrackAdapterForPlaylist(tracks)
        binding.rvTracksList.adapter = adapter

        val playlistId = arguments?.getInt(PLAYLIST_KEY_ID)
        viewModel.updatePlaylist(playlistId!!)

        viewModel.playlist().observe(viewLifecycleOwner){
            tracks.clear()
            tracks.addAll(it.tracks)
            writeDataInFragment(it)
            showRecyclerViewOrMessageError(tracks)
            adapter.notifyDataSetChanged()
        }

        adapter.itemClickListener = { track ->
            if (viewModel.clickDebounce()) {
                startPlayerFragment(track)
            }
        }

        adapter.itemLongClickListener = { track ->
            val dialog = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Удалить трек?")
                setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
                setPositiveButton("Удалить") { _, _ ->
                    viewModel.deleteTrackFromPlaylist(track)
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
            }
            dialog.show()
        }

        binding.ivButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        viewModel.onDestroy()
        super.onDestroyView()
    }

    private fun writeDataInFragment(playlist: PlayList) {
        binding.tvPlaylistName.text = playlist.playlistName
        binding.tvPlaylistDescription.text = playlist.playlistDescription
        binding.tvPlaylistDuration.text = calculateMin(playlist.tracks)
        binding.tvPlaylistCount.text =
            playlist.tracksCount.toString() + " " + requireContext().resources.getQuantityString(
                R.plurals.plurals_track, playlist.tracksCount
            )

        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

        Glide.with(requireContext())
            .load(playlist.playlistImage?.let { playlistImage -> File(filePath, playlistImage) })
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(binding.ivPlaylistImage)
    }

    private fun calculateMin(list: ArrayList<Track>): String {
        var timeMillis: Long = 0
        list.forEach { timeMillis += it.trackTimeMillis }
        val result = SimpleDateFormat("mm", Locale.getDefault()).format(timeMillis)
        return result + " " + requireContext().resources.getQuantityString(
            R.plurals.plurals_min, result.toInt()
        )
    }

    private fun showRecyclerViewOrMessageError(tracks: ArrayList<Track>) {
        binding.tvEmptyList.isVisible = tracks.isEmpty()
        binding.rvTracksList.isVisible = tracks.isNotEmpty()
    }

    private fun startPlayerFragment(track: Track) {              //Запустили активити с плеером
        val bundle = Bundle()
        bundle.putParcelable(TRACK_KEY, track)
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment2, bundle)
    }

    companion object {
        private const val PLAYLIST_KEY_ID = "playlistId"
        private const val DIRECTORY = "album_images"
        private const val TRACK_KEY = "track"
    }
}