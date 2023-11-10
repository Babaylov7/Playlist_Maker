package com.example.playlistmaker.presentation.ui.player

import android.media.MediaPlayer
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.databinding.PlayerActivityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: PlayerActivityBinding
    private var trackAddInQueue = false
    private var trackAddInFavorite = false
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

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
            playbackControl()
        }

        binding.buttonFavorite.setOnClickListener {
            changeButtonFavoriteImage()
        }

        preparePlayer(track)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(updateTimeOfPlay())             //handler.removeCallbacks(searchRunnable)
        mediaPlayer.release()
    }

    private fun updateTimeOfPlay(): Runnable {                   //Обновленеи времени проигрования трека
        return object : Runnable {
            override fun run() {
                val currentTime = mediaPlayer.currentPosition

                if (playerState == STATE_PLAYING) {
                    binding.timeOfPlay.text =
                        SimpleDateFormat("m:ss", Locale.getDefault()).format(currentTime)
                    mainThreadHandler.postDelayed(this, UPDATE)
                } else if (playerState == STATE_PAUSED) {
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

    private fun preparePlayer(track: Track) {
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
            showMassage()
        }
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
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.buttonPlay.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        mainThreadHandler.post(updateTimeOfPlay())
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.buttonPlay.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
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
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE = 250L
    }
}