package com.example.playlistmaker.presentation.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.playlist.models.PlayList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryPlaylistsViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null

    private val _playLists: MutableLiveData<List<PlayList>> =
        MutableLiveData<List<PlayList>>()

    fun playLists(): LiveData<List<PlayList>> = _playLists

    fun checkPlayListsInDb() {
        viewModelScope.launch {
            playListInteractor
                .getPlayLists()
                .collect { result ->
                    _playLists.postValue(result)
                }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob = viewModelScope.launch {
                isClickAllowed = false
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}



