package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.models.Track

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