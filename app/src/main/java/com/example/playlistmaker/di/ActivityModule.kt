package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.ui.library.view_model.LibraryFavoriteFragmentViewModel
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryPlaylistsFragmentViewModel
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryViewModel
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val activityModule = module {

    viewModel {
        PlayerViewModel(get(), get(), get())
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
        LibraryFavoriteFragmentViewModel(get())
    }

    viewModel {
        LibraryPlaylistsFragmentViewModel()
    }
}