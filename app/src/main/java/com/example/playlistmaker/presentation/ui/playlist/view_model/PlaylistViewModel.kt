package com.example.playlistmaker.presentation.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.playlist.PlaylistSharingInteractor
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playListInteractor: PlayListInteractor,
    private val playlistSharingInteractor: PlaylistSharingInteractor
) : ViewModel() {
    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var dbJob: Job? = null

    private val _playlist: MutableLiveData<PlayList> = MutableLiveData<PlayList>()
    fun playlist(): LiveData<PlayList> = _playlist

    fun deleteTrackFromPlaylist(track: Track) {
        val arrayOfTracks = _playlist.value?.tracks
        arrayOfTracks!!.remove(track)
        viewModelScope.launch {
            playListInteractor.updateTracks(
                _playlist.value?.id!!,
                arrayOfTracks,
                _playlist.value?.tracksCount?.minus(1) ?: 0
            )
            updatePlaylist(_playlist.value?.id!!)
        }
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
        isClickAllowed = true
    }

    fun updatePlaylist(id: Int) {
        viewModelScope.launch {
            playListInteractor
                .getPlaylist(id)
                .collect { result ->
                    _playlist.postValue(result)
                }
        }
    }

    fun sharePlaylist(count: String) {
        val playlistInfo = getPlaylistInfo(count)
        playlistSharingInteractor.shareTrackList(playlistInfo)
    }

    private fun getPlaylistInfo(count: String): String {
        val name = _playlist.value!!.playlistName + "\n"
        val description =
            if (_playlist.value!!.playlistDescription.isNullOrEmpty()) "" else _playlist.value!!.playlistDescription + "\n"
        val tracksCount = _playlist.value!!.tracksCount.toString() + " " + count + "\n"
        var counter = 0
        var tracksInfo = ""
        _playlist.value!!.tracks.forEach { track ->
            counter++
            tracksInfo += counter.toString() + ". " + track.artistName + " - " + track.trackName + " (" + SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis) + ")"
            if (counter != _playlist.value!!.tracksCount) {
                tracksInfo += "\n"
            }
        }
        return name + description + tracksCount + tracksInfo
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playListInteractor.deletePlayList(_playlist.value!!.id)
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}