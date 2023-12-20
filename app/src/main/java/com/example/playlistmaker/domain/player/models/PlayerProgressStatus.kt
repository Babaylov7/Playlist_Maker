package com.example.playlistmaker.domain.player.models

data class PlayerProgressStatus(
    val mediaPlayerStatus: MediaPlayerStatus,
    val currentPosition: Int = 0
)
