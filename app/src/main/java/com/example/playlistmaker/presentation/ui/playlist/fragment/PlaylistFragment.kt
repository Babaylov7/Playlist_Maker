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
import com.example.playlistmaker.presentation.ui.playlist.PlaylistAdapterForPlaylist
import com.example.playlistmaker.presentation.ui.playlist.TrackAdapterForPlaylist
import com.example.playlistmaker.presentation.ui.playlist.view_model.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : BindingFragment<PlaylistFragmentBinding>() {

    private val viewModel by viewModel<PlaylistViewModel>()
    private val tracks: ArrayList<Track> = ArrayList<Track>()
    private val playlistForBottomSheet: ArrayList<PlayList> = ArrayList<PlayList>()

    private lateinit var trackAdapter: TrackAdapterForPlaylist
    private lateinit var playlistAdapter: PlaylistAdapterForPlaylist

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PlaylistFragmentBinding {
        return PlaylistFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackAdapter = TrackAdapterForPlaylist(tracks)
        playlistAdapter = PlaylistAdapterForPlaylist(playlistForBottomSheet)
        binding.rvTracksList.adapter = trackAdapter
        binding.rvAlbum.adapter = playlistAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val playlistId = arguments?.getInt(PLAYLIST_KEY_ID)
        viewModel.updatePlaylist(playlistId!!)

        viewModel.playlist().observe(viewLifecycleOwner){
            tracks.clear()
            tracks.addAll(it.tracks)
            playlistForBottomSheet.clear()
            playlistForBottomSheet.add(it)
            writeDataInFragment(it)
            showRecyclerViewOrMessageError(tracks)
            trackAdapter.notifyDataSetChanged()
            playlistAdapter.notifyDataSetChanged()
        }

        trackAdapter.itemClickListener = { track ->
            if (viewModel.clickDebounce() && binding.tracksBottomSheet.isEnabled) {
                startPlayerFragment(track)
            }
        }

        trackAdapter.itemLongClickListener = { track ->
            val dialog = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(getString(R.string.del_track))
                setMessage(getString(R.string.del_track_qestion))
                setPositiveButton(getString(R.string.del)) { _, _ ->
                    viewModel.deleteTrackFromPlaylist(track)
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
            }
            dialog.show()
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.ivMenu.isEnabled = true
                        binding.ivShare.isEnabled = true
                        binding.tracksBottomSheet.isEnabled = true
                    }
                    else -> {
                        binding.overlay.isVisible = true
                        binding.ivMenu.isEnabled = false
                        binding.ivShare.isEnabled = false
                        binding.tracksBottomSheet.isEnabled = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.ivButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivMenu.setOnClickListener {
            showBottomSheet()
        }

        binding.ivShare.setOnClickListener {
            handlingShareClick()
        }

        binding.tvShare.setOnClickListener {
            handlingShareClick()
        }

        binding.tvEditInf.setOnClickListener {
            startPlaylistEditingFragment()
        }

        binding.tvDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Хотите удалить «${viewModel.playlist().value!!.playlistName}»?") // Описание диалога
                .setNegativeButton("Нет") { dialog, which -> // Добавляет кнопку «Нет»
                }
                .setPositiveButton("Да") { dialog, which -> // Добавляет кнопку «Да»
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }
                .show()
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

    private fun startPlaylistEditingFragment() {
        val bundle = Bundle()
        bundle.putInt(PLAYLIST_KEY, viewModel.playlist().value!!.id)
        findNavController().navigate(R.id.action_playlistFragment_to_playlistEditingFragment, bundle)
    }

    private fun showBottomSheet() {
        BottomSheetBehavior.from(binding.menuBottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun handlingShareClick(){
        if (viewModel.playlist().value!!.tracks.isEmpty()){
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("В этом плейлисте нет списка треков, которым можно поделиться")
                .setNeutralButton("ОК"
                ) { dialog, which ->
                    //ничего не делаем
                }
                .show()
        } else {
            viewModel.sharePlaylist(requireContext().resources.getQuantityString(R.plurals.plurals_track, viewModel.playlist().value!!.tracksCount))
        }
    }

    companion object {
        private const val PLAYLIST_KEY_ID = "playlistId"
        private const val DIRECTORY = "album_images"
        private const val TRACK_KEY = "track"
        private const val PLAYLIST_KEY = "playlist"
    }
}