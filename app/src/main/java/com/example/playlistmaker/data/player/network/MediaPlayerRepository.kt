package com.example.playlistmaker.data.player.network

import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.models.Track

interface MediaPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getPlayerProgressStatus(): PlayerProgressStatus
    fun destroyPlayer()
}