package com.example.playlistmaker.domain.player.models

import com.example.playlistmaker.domain.player.models.MediaPlayerStatus

data class PlayerProgressStatus(
    val mediaPlayerStatus: MediaPlayerStatus,
    val currentPosition: Int = 0
)
