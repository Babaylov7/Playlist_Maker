package com.example.playlistmaker.presentation.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.playlist.PlayList
import com.example.playlistmaker.presentation.ui.search.view_model.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryPlaylistsViewModel(private val playListInteractor: PlayListInteractor): ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null

    private val playListsLiveData: MutableLiveData<List<PlayList>> = MutableLiveData<List<PlayList>>()
    fun getPlayLists(): LiveData<List<PlayList>> = playListsLiveData

    fun checkPlayListsInDb(){
        viewModelScope.launch {
//            val playLists = playListInteractor.getPlayLists()
//            playListsLiveData.postValue(playLists)
            playListInteractor
                .getPlayLists()
                .collect{
                    result -> playListsLiveData.postValue(result)
                }

        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob = viewModelScope.launch {
                isClickAllowed = false
                delay(LibraryPlaylistsViewModel.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}



