package com.example.playlistmaker.presentation.ui.new_playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.playlist.PlayList
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playListInteractor: PlayListInteractor
): ViewModel() {


    fun createNewPlayList(albumName: String, description: String, albumImage: String?) {
        val playList = PlayList(
            id = 0,
            playlistName = albumName,
            playlistDescription = description,
            playlistImage = albumImage,
            tracksId = "",
            tracksCount = 0
        )

        viewModelScope.launch {
            playListInteractor.insertPlaylist(playList)
        }
    }
}