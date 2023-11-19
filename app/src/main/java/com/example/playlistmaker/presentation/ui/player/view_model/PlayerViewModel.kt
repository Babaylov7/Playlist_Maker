package com.example.playlistmaker.presentation.ui.player.view_model

import android.os.Build.VERSION.SDK_INT
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(val context: Context) : ViewModel() {
    private var mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    var playerProgressStatus: MutableLiveData<PlayerProgressStatus> = MutableLiveData(getPlayerProgressStatus())


    fun onCreate(track: Track){
        mediaPlayerInteractor.preparePlayer(track)
        playerProgressStatus.value = getPlayerProgressStatus()
    }

    private fun getPlayerProgressStatus(): PlayerProgressStatus {
        return mediaPlayerInteractor.getPlayerProgressStatus()
    }

    fun pauseMediaPlayer(){
        mediaPlayerInteractor.pausePlayer()
    }
    fun destroyMediaPlayer(){
        mediaPlayerInteractor.destroyPlayer()
    }


    fun updateTimeOfPlay(): Runnable {                   //Обновленеи времени проигрования трека
        return object : Runnable {
            override fun run() {
                playerProgressStatus.value = getPlayerProgressStatus()

                if(playerProgressStatus.value!!.mediaPlayerStatus == MediaPlayerStatus.STATE_PLAYING) {
//                    binding.timeOfPlay.text =
//                        SimpleDateFormat("m:ss", Locale.getDefault()).format(playerProgressStatus.value!!.currentPosition)
                    mainThreadHandler.postDelayed(this, UPDATE)
                } else if(playerProgressStatus.value!!.mediaPlayerStatus == MediaPlayerStatus.STATE_PAUSED){
                    mainThreadHandler.removeCallbacks(updateTimeOfPlay())
                } else {
//                    binding.timeOfPlay.text = "0:00"
//                    binding.buttonPlay.setImageResource(R.drawable.button_play)
                    mainThreadHandler.removeCallbacks(updateTimeOfPlay())
                }
            }
        }
    }


    fun playbackControl() {
        when (playerProgressStatus.value!!.mediaPlayerStatus){
            MediaPlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            MediaPlayerStatus.STATE_PREPARED, MediaPlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            MediaPlayerStatus.STATE_ERROR -> {
                showMassage()
            }
            MediaPlayerStatus.STATE_DEFAULT ->{
            }
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        //binding.buttonPlay.setImageResource(R.drawable.button_pause)
        mainThreadHandler.post(updateTimeOfPlay())
        playerProgressStatus.value = getPlayerProgressStatus()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        //binding.buttonPlay.setImageResource(R.drawable.button_play)
        playerProgressStatus.value = getPlayerProgressStatus()
    }


    fun showMassage(){
        Toast.makeText(context, context.getString(R.string.audio_file_not_available), Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val UPDATE = 250L
    }

}