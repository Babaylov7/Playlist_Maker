package com.example.playlistmaker.presentation.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LibraryFragmentBinding
import com.example.playlistmaker.presentation.ui.library.LibraryViewPageAdapter
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    private var _binding: LibraryFragmentBinding? = null
    private val binding: LibraryFragmentBinding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    private val viewModel by viewModel<LibraryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LibraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpLibrary.adapter = LibraryViewPageAdapter(fragmentManager = childFragmentManager, lifecycle = lifecycle)

        tabMediator = TabLayoutMediator(binding.tlLibrary, binding.vpLibrary){
                tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.featured_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }
}