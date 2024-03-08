package com.example.playlistmaker.presentation.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playListInteractor: PlayListInteractor

) : ViewModel() {
    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var dbJob: Job? = null

    private val _playlist: MutableLiveData<PlayList> = MutableLiveData<PlayList>()
    fun playlist(): LiveData<PlayList> = _playlist

    fun deleteTrackFromPlaylist(track: Track){

    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob = viewModelScope.launch(Dispatchers.IO) {
                isClickAllowed = false
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    fun onDestroy() {
        clickJob?.cancel()
        dbJob?.cancel()
    }

    fun updatePlaylist(id: Int){
        viewModelScope.launch {
            playListInteractor
                .getPlaylist(id)
                .collect { result ->
                    _playlist.postValue(result)
                }
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}