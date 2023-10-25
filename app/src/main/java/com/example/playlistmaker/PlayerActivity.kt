package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlayerActiviityBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: PlayerActiviityBinding
    private var trackAddInQueue = false
    private var trackAddInFavorite = false
    private var trackOnPause = false
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActiviityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = if (SDK_INT >= 33) {                        //Проверяем версию SDK и в зависимости от верстии применяем тот или иной метод для работы с intent
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
        mediaPlayer.release()
    }

    private fun writeDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.songDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text =
            if (track.collectionName!!.isNotEmpty()) track.collectionName else ""
        binding.songYear.text = if (track.releaseDate!!.isNotEmpty()) track.releaseDate?.substring(
            0,
            4
        ) else "Not found"
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        val artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(binding.albumImage)
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
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
        when(playerState) {
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
    }
}