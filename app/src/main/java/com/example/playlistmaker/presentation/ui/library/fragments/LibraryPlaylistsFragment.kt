package com.example.playlistmaker.presentation.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LibraryPlaylistsFragmentBinding
import com.example.playlistmaker.domain.playlist.PlayList
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.BindingFragment
import com.example.playlistmaker.presentation.ui.library.PlayListAdapter
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryPlaylistsViewModel
import com.example.playlistmaker.presentation.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistsFragment: BindingFragment<LibraryPlaylistsFragmentBinding>() {

    private lateinit var adapter: PlayListAdapter
    private lateinit var playLists: ArrayList<PlayList>

    private val viewModel by viewModel<LibraryPlaylistsViewModel>()

    private val onClick: (playList: PlayList) -> Unit = {
        if (viewModel.clickDebounce()) {
           // startPlayerActivity(it)   думаю пригодится на следующем ДЗ
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LibraryPlaylistsFragmentBinding {
        return LibraryPlaylistsFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playLists = ArrayList<PlayList>()
        adapter = PlayListAdapter(playLists, onClick)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        viewModel.checkPlayListsInDb()
        viewModel.getPlayLists().observe(viewLifecycleOwner){
            showPlayLists(it)
        }


        binding.bNewPlaylist.setOnClickListener {
            (activity as? MainActivity)?.hideNavBar()
            findNavController().navigate( R.id.action_libraryFragment_to_newPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showNawBar()
    }


    private fun showPlayLists(findPlayLists: List<PlayList>){
        if (findPlayLists.isEmpty()){
            showErrorImage()
        } else {
            hideErrorImage()
            playLists.clear()
            playLists.addAll(findPlayLists)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showErrorImage() {
        binding.recyclerView.visibility = View.GONE
        binding.ivMessageImage.visibility = View.VISIBLE
        binding.tvMessageError.visibility = View.VISIBLE
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
    private fun hideErrorImage(){
        binding.recyclerView.visibility = View.VISIBLE
        binding.ivMessageImage.visibility = View.GONE
        binding.tvMessageError.visibility = View.GONE
    }

    companion object{
        fun newInstance() = LibraryPlaylistsFragment()
    }
}