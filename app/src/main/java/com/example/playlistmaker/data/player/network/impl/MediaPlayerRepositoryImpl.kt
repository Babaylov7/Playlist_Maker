package com.example.playlistmaker.data.player.network.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.models.Track

class MediaPlayerRepositoryImpl() : MediaPlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = MediaPlayerStatus.STATE_DEFAULT

    override fun preparePlayer(track: Track) {
        try {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = MediaPlayerStatus.STATE_PREPARED
                Log.d("NewLog", "Статус плеера $playerState")
            }
            mediaPlayer.setOnCompletionListener {
                playerState = MediaPlayerStatus.STATE_PREPARED
                Log.d("NewLog", "Статус плеера $playerState")
            }

        } catch (e: Exception) {
            playerState = MediaPlayerStatus.STATE_ERROR
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerStatus.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerStatus.STATE_PAUSED
    }

    override fun getPlayerProgressStatus(): PlayerProgressStatus {
        val currentPosition = mediaPlayer.currentPosition
        return when (playerState) {
            MediaPlayerStatus.STATE_PLAYING -> {
                PlayerProgressStatus(
                    mediaPlayerStatus = MediaPlayerStatus.STATE_PLAYING,
                    currentPosition = currentPosition
                )
            }

            MediaPlayerStatus.STATE_PAUSED -> {
                PlayerProgressStatus(
                    mediaPlayerStatus = MediaPlayerStatus.STATE_PAUSED,
                    currentPosition = currentPosition
                )
            }

            MediaPlayerStatus.STATE_PREPARED -> {
                PlayerProgressStatus(
                    mediaPlayerStatus = MediaPlayerStatus.STATE_PREPARED,
                    currentPosition = currentPosition
                )
            }

            MediaPlayerStatus.STATE_DEFAULT -> {
                PlayerProgressStatus(
                    mediaPlayerStatus = MediaPlayerStatus.STATE_DEFAULT,
                    currentPosition = currentPosition
                )
            }

            MediaPlayerStatus.STATE_ERROR -> {
                PlayerProgressStatus(
                    mediaPlayerStatus = MediaPlayerStatus.STATE_ERROR,
                    currentPosition = 0
                )
            }
        }
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }
}