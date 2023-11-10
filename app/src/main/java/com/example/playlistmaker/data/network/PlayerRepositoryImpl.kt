package com.example.playlistmaker.data.network

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.media_player.PlayerRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.player.PlayerActivity
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl() : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    override fun preparePlayer(track: Track) {
        try{
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        } catch (e: Exception)  {
            //showMassage()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        binding.buttonPlay.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        mainThreadHandler.post(updateTimeOfPlay())
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        binding.buttonPlay.setImageResource(R.drawable.button_play)
        playerState = PlayerActivity.STATE_PAUSED
    }

    override fun updateTimeOfPlay() {
        val currentTime = mediaPlayer.currentPosition

        if (playerState == PlayerActivity.STATE_PLAYING) {
            binding.timeOfPlay.text =
                SimpleDateFormat("m:ss", Locale.getDefault()).format(currentTime)
            mainThreadHandler.postDelayed(this, PlayerActivity.UPDATE)
        } else if (playerState == PlayerActivity.STATE_PAUSED) {
            mainThreadHandler.removeCallbacks(updateTimeOfPlay())
        } else {
            binding.timeOfPlay.text = "0:00"
            binding.buttonPlay.setImageResource(R.drawable.button_play)
            mainThreadHandler.removeCallbacks(updateTimeOfPlay())
        }
    }

    private companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE = 250L
    }
}