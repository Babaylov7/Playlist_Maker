package com.example.playlistmaker.presentation.ui.player.activity

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: PlayerActivityBinding
    private lateinit var playerProgressStatus: PlayerProgressStatus
    private var trackAddInQueue = false
    private var trackAddInFavorite = false

    private var mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timeOfPlay.text = "0:00"

        val track =
            if (SDK_INT >= 33) {                        //Проверяем версию SDK и в зависимости от верстии применяем тот или иной метод для работы с intent
                intent.getParcelableExtra("track", Track::class.java)!!
            } else {
                intent.getParcelableExtra<Track>("track")!!
            }

        writeDataInActivity(track)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonQueue.setOnClickListener {
            changeButtonQueueImage()
        }

        binding.buttonPlay.setOnClickListener {
            playerProgressStatus = mediaPlayerInteractor.getPlayerProgressStatus()
            playbackControl()
        }

        binding.buttonFavorite.setOnClickListener {
            changeButtonFavoriteImage()
        }

        mediaPlayerInteractor.preparePlayer(track)
        playerProgressStatus = mediaPlayerInteractor.getPlayerProgressStatus()
        if (playerProgressStatus.mediaPlayerStatus == MediaPlayerStatus.STATE_ERROR) {
            showMassage()
        }
    }

    override fun onPause() {
        mediaPlayerInteractor.pausePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        mediaPlayerInteractor.destroyPlayer()
        mainThreadHandler.removeCallbacks(updateTimeOfPlay())
        super.onDestroy()
    }

    private fun updateTimeOfPlay(): Runnable {                   //Обновленеи времени проигрования трека
        return object : Runnable {
            override fun run() {
                playerProgressStatus = mediaPlayerInteractor.getPlayerProgressStatus()

                if(playerProgressStatus.mediaPlayerStatus == MediaPlayerStatus.STATE_PLAYING) {
                    binding.timeOfPlay.text =
                        SimpleDateFormat("m:ss", Locale.getDefault()).format(playerProgressStatus.currentPosition)
                    mainThreadHandler.postDelayed(this, UPDATE)
                } else if(playerProgressStatus.mediaPlayerStatus == MediaPlayerStatus.STATE_PAUSED){
                    mainThreadHandler.removeCallbacks(updateTimeOfPlay())
                } else {
                    binding.timeOfPlay.text = "0:00"
                    binding.buttonPlay.setImageResource(R.drawable.button_play)
                    mainThreadHandler.removeCallbacks(updateTimeOfPlay())
                }
            }
        }
    }

    private fun writeDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.songDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName
        binding.songYear.text = if (!track.releaseDate.equals("unknown")) track.releaseDate.substring(
            0,
            4
        ) else "Not found"
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(binding.albumImage)
    }

    private fun showMassage(){
        Toast.makeText(this, getString(R.string.audio_file_not_available), Toast.LENGTH_LONG).show()
    }

    private fun changeButtonQueueImage() {
        if (trackAddInQueue) {
            trackAddInQueue = false
            binding.buttonQueue.setImageResource(R.drawable.button_queue)
        } else {
            trackAddInQueue = true
            binding.buttonQueue.setImageResource(R.drawable.button_add_in_queue)
        }
    }

    private fun playbackControl() {
        when (playerProgressStatus.mediaPlayerStatus){
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
        binding.buttonPlay.setImageResource(R.drawable.button_pause)
        mainThreadHandler.post(updateTimeOfPlay())
        playerProgressStatus = mediaPlayerInteractor.getPlayerProgressStatus()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        binding.buttonPlay.setImageResource(R.drawable.button_play)
        playerProgressStatus = mediaPlayerInteractor.getPlayerProgressStatus()
    }

    private fun changeButtonFavoriteImage() {
        if (trackAddInFavorite) {
            trackAddInFavorite = false
            binding.buttonFavorite.setImageResource(R.drawable.button_favorite)
        } else {
            trackAddInFavorite = true
            binding.buttonFavorite.setImageResource(R.drawable.button_add_in_favorite)
        }
    }

    companion object {
        private const val UPDATE = 250L
    }
}