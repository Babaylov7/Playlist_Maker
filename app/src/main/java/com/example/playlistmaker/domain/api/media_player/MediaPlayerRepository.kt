package com.example.playlistmaker.domain.api.media_player

import com.example.playlistmaker.domain.models.PlayerProgressStatus
import com.example.playlistmaker.domain.models.Track

interface MediaPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getPlayerProgressStatus(): PlayerProgressStatus
    fun destroyPlayer()
}