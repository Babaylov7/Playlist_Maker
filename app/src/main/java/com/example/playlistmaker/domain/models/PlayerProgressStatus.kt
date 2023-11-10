package com.example.playlistmaker.domain.models

data class PlayerProgressStatus(
    val mediaPlayerStatus: MediaPlayerStatus,
    val currentPosition: Int = 0
)
