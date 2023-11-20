package com.example.playlistmaker.presentation.ui.player.activity

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: PlayerActivityBinding
    private lateinit var playerProgressStatus: PlayerProgressStatus
    private var trackAddInQueue = false
    private var trackAddInFavorite = false

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timeOfPlay.text = getString(R.string.player_default_time)

        viewModel =
            ViewModelProvider(this, PlayerViewModelFactory(this, Creator.provideMediaPlayerInteractor()))[PlayerViewModel::class.java]

        playerProgressStatus = viewModel._playerProgressStatus.value!!


        val track =
            if (SDK_INT >= 33) {                        //Проверяем версию SDK и в зависимости от верстии применяем тот или иной метод для работы с intent
                intent.getParcelableExtra("track", Track::class.java)!!
            } else {
                intent.getParcelableExtra<Track>("track")!!
            }

        writeDataInActivity(track)
        viewModel.onCreate(track)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonQueue.setOnClickListener {
            changeButtonQueueImage()
        }

        binding.buttonFavorite.setOnClickListener {
            changeButtonFavoriteImage()
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel._playerProgressStatus.observe(this) {
            playbackControl()
        }

        if (playerProgressStatus.mediaPlayerStatus == MediaPlayerStatus.STATE_ERROR) {
            viewModel.showMassage()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyMediaPlayer()
    }

    private fun playbackControl() {
        when (playerProgressStatus.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                binding.buttonPlay.setImageResource(R.drawable.button_play)
                binding.timeOfPlay.text =
                    SimpleDateFormat(
                        "m:ss",
                        Locale.getDefault()
                    ).format(viewModel._playerProgressStatus.value!!.currentPosition)
            }

            MediaPlayerStatus.STATE_PREPARED, MediaPlayerStatus.STATE_PAUSED -> {
                binding.buttonPlay.setImageResource(R.drawable.button_pause)
                binding.timeOfPlay.text = "0:00"
                binding.buttonPlay.setImageResource(R.drawable.button_play)
            }

            MediaPlayerStatus.STATE_ERROR, MediaPlayerStatus.STATE_DEFAULT -> {

            }
        }
    }

    private fun writeDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.songDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName
        binding.songYear.text =
            if (!track.releaseDate.equals("unknown")) track.releaseDate.substring(
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

    private fun changeButtonFavoriteImage() {
        if (trackAddInFavorite) {
            trackAddInFavorite = false
            binding.buttonFavorite.setImageResource(R.drawable.button_favorite)
        } else {
            trackAddInFavorite = true
            binding.buttonFavorite.setImageResource(R.drawable.button_add_in_favorite)
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
}