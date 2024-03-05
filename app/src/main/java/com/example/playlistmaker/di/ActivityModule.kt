package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.ui.library.view_model.LibraryFavoriteViewModel
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryPlaylistsViewModel
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryViewModel
import com.example.playlistmaker.presentation.ui.new_playlist.view_model.NewPlaylistViewModel
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {

    viewModel {
        PlayerViewModel(get(), get(), get(), get())
    }
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        LibraryViewModel()
    }

    viewModel {
        LibraryFavoriteViewModel(get())
    }

    viewModel {
        LibraryPlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }
}