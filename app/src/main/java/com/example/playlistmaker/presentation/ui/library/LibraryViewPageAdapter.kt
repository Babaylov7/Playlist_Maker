package com.example.playlistmaker.presentation.ui.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.ui.library.fragments.LibraryFavoriteFragment
import com.example.playlistmaker.presentation.ui.library.fragments.LibraryPlaylistsFragment

class LibraryViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> LibraryFavoriteFragment()
            else -> LibraryPlaylistsFragment()
        }
    }

}