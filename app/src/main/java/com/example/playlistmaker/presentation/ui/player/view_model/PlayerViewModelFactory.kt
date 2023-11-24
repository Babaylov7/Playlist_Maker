package com.example.playlistmaker.presentation.ui.player.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.player.MediaPlayerInteractor

class PlayerViewModelFactory(
    val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            mediaPlayerInteractor = mediaPlayerInteractor
        ) as T
    }
}