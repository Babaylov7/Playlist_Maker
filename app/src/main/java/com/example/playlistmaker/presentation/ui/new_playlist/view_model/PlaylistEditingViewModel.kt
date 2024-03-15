package com.example.playlistmaker.presentation.ui.new_playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.playlist.models.PlayList
import kotlinx.coroutines.launch

class PlaylistEditingViewModel(
    private val playListInteractor: PlayListInteractor
): NewPlaylistViewModel(playListInteractor) {

    private val _playlist: MutableLiveData<PlayList> = MutableLiveData<PlayList>()
    fun playlist(): LiveData<PlayList> = _playlist

    fun updatePlaylist(id: Int) {
        viewModelScope.launch {
            playListInteractor
                .getPlaylist(id)
                .collect { result ->
                    _playlist.postValue(result)
                }
        }
    }

    fun updatePlaylistInfo(name: String, description: String){
        viewModelScope.launch {
            playListInteractor
                .updatePlaylistInfo(
                    _playlist.value!!.id,
                    name,
                    description
                )
        }
    }
}