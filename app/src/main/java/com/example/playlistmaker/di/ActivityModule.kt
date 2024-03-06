package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.ui.library.view_model.LibraryFavoriteViewModel
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryPlaylistsViewModel
import com.example.playlistmaker.presentation.ui.library.view_model.LibraryViewModel
import com.example.playlistmaker.presentation.ui.new_playlist.view_model.NewPlaylistViewModel
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.ui.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val activityModule = module {

    viewModel {
        PlayerViewModel(
            get(named(MEDIA_PLAYER_INTERACTOR)),
            get(named(FAVORITE_TRACKS_INTERACTOR)),
            get(named(SEARCH_HISTORY_INTERACTOR)),
            get(named(PLAY_LIST_INTERACTOR))
        )
    }
    viewModel {
        SearchViewModel(
            get(named(SEARCH_HISTORY_INTERACTOR)),
            get(named(TRACK_INTERACTOR))
        )
    }

    viewModel {
        SettingsViewModel(
            get(named(SHARING_INTERACTOR)),
            get(named(SAVED_SETTINGS_INTERACTOR))
        )
    }

    viewModel {
        LibraryViewModel()
    }

    viewModel {
        LibraryFavoriteViewModel(
            get(named(FAVORITE_TRACKS_INTERACTOR))
        )
    }

    viewModel {
        LibraryPlaylistsViewModel(
            get(named(PLAY_LIST_INTERACTOR))
        )
    }

    viewModel {
        NewPlaylistViewModel(
            get(named(PLAY_LIST_INTERACTOR))
        )
    }

    viewModel {
        PlaylistViewModel(
            get(named(PLAY_LIST_INTERACTOR))
        )
    }
}