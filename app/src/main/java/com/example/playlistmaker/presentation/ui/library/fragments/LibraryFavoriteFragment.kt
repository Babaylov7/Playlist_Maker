package com.example.playlistmaker.presentation.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LibraryFavoriteFragmentBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryFavoriteViewModel
import com.example.playlistmaker.presentation.ui.main.MainActivity
import com.example.playlistmaker.presentation.ui.search.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFavoriteFragment : BindingFragment<LibraryFavoriteFragmentBinding>() {

    private lateinit var adapter: TrackAdapter
    private lateinit var tracks: ArrayList<Track>
    private val viewModel by viewModel<LibraryFavoriteViewModel>()

    private val onClick: (track: Track) -> Unit = {
        if (viewModel.clickDebounce()) {
            startPlayerActivity(it)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LibraryFavoriteFragmentBinding {
        return LibraryFavoriteFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracks = ArrayList<Track>()
        adapter = TrackAdapter(tracks, onClick)
        viewModel.onCreate()
        binding.rvFavoriteTracks.adapter = adapter

        viewModel.getFavoriteTracks().observe(viewLifecycleOwner) {
            showFavoriteTracksList(it)
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showNawBar()
        viewModel.onCreate()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

    private fun showFavoriteTracksList(tracksFromDb: List<Track>) {
        if (tracksFromDb.isEmpty()) {
            binding.ivImageError.visibility = View.VISIBLE
            binding.tvMessageError.visibility = View.VISIBLE
            binding.rvFavoriteTracks.visibility = View.GONE
            showErrorImage()
            tracks.clear()
        } else {
            binding.ivImageError.visibility = View.GONE
            binding.tvMessageError.visibility = View.GONE
            binding.rvFavoriteTracks.visibility = View.VISIBLE
            tracks.clear()
            tracks.addAll(tracksFromDb)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showErrorImage() {

        if (requireContext().isNightModeOn()) {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_dark)
                .into(binding.ivImageError)
        } else {
            Glide.with(this)
                .load(R.drawable.nothing_was_found_light)
                .into(binding.ivImageError)
        }
    }

    private fun startPlayerActivity(track: Track) {              //Запустили активити с плеером

        (activity as? MainActivity)?.hideNavBar()

        val bundle = Bundle()
        bundle.putParcelable(LibraryFavoriteFragment.TRACK_KEY, track)
        findNavController().navigate( R.id.action_libraryFragment_to_playerFragment, bundle)
    }

    companion object {
        fun newInstance() = LibraryFavoriteFragment()
        private const val TRACK_KEY = "track"
    }


}