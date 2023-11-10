package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.media_player.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.media_player.MediaPlayerRepository
import com.example.playlistmaker.domain.models.PlayerProgressStatus
import com.example.playlistmaker.domain.models.Track

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository):
    MediaPlayerInteractor {
    override fun preparePlayer(track: Track) {
        mediaPlayerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun getPlayerProgressStatus(): PlayerProgressStatus {
        return mediaPlayerRepository.getPlayerProgressStatus()
    }

    override fun destroyPlayer() {
        mediaPlayerRepository.destroyPlayer()
    }
}