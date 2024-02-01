package com.example.playlistmaker.presentation.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(val mediaPlayerInteractor: MediaPlayerInteractor) :
    ViewModel() {
    private var updateTimeOfPlayJob: Job? = null

    private val playerProgressStatus: MutableLiveData<PlayerProgressStatus> =
        MutableLiveData(updatePlayerProgressStatus())

    fun getPlayerProgressStatus(): LiveData<PlayerProgressStatus> = playerProgressStatus

    fun onCreate(track: Track) {
        mediaPlayerInteractor.preparePlayer(track)
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    private fun updatePlayerProgressStatus(): PlayerProgressStatus {
        return mediaPlayerInteractor.getPlayerProgressStatus()
    }

    fun pauseMediaPlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun destroyMediaPlayer() {
        updateTimeOfPlayJob?.cancel()
        mediaPlayerInteractor.destroyPlayer()
    }

    private fun updateTimeOfPlay() {                   //Обновленеи времени проигрования трека
        playerProgressStatus.value = updatePlayerProgressStatus()

        if (playerProgressStatus.value!!.mediaPlayerStatus == MediaPlayerStatus.STATE_PLAYING) {
            updateTimeOfPlayJob = viewModelScope.launch {
                delay(UPDATE)
                updateTimeOfPlay()
            }
        } else if (playerProgressStatus.value!!.mediaPlayerStatus == MediaPlayerStatus.STATE_PAUSED) {
            updateTimeOfPlayJob?.cancel()
        } else {
            updateTimeOfPlayJob?.cancel()
        }


    }

    fun playbackControl() {
        playerProgressStatus.value = updatePlayerProgressStatus()
        when (playerProgressStatus.value!!.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }

            MediaPlayerStatus.STATE_PREPARED, MediaPlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }

            MediaPlayerStatus.STATE_ERROR, MediaPlayerStatus.STATE_DEFAULT -> {
            }
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        updateTimeOfPlay()
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    companion object {
        private const val UPDATE = 250L
    }
}