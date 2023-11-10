package com.example.playlistmaker.domain.api.media_player

import com.example.playlistmaker.domain.models.Track

interface PlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun updateTimeOfPlay()
}