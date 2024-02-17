package com.example.playlistmaker.presentation.ui.player.activity

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.databinding.PlayerActivityBinding
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.presentation.isNightModeOn
import com.example.playlistmaker.presentation.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: PlayerActivityBinding
    private var trackAddInQueue = false

    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timeOfPlay.text = getString(R.string.player_default_time)

        val track =
            if (SDK_INT >= 33) {                        //Проверяем версию SDK и в зависимости от верстии применяем тот или иной метод для работы с intent
                intent.getParcelableExtra(TRACK_KEY, Track::class.java)!!
            } else {
                intent.getParcelableExtra<Track>(TRACK_KEY)!!
            }

        writeDataInActivity(track)
        viewModel.onCreate(track)

        viewModel.getPlayerProgressStatus().observe(this) { playerProgressStatus ->
            playbackControl(playerProgressStatus)
        }
        viewModel.favoriteStatus().observe(this){favoriteStatus ->
            changeButtonFavoriteImage(favoriteStatus)

        }

        binding.ivButtonBack.setOnClickListener {
            finish()
        }

        binding.buttonQueue.setOnClickListener {
            changeButtonQueueImage()
        }

        binding.ivButtonFavorite.setOnClickListener {
            viewModel.clickButtonFavorite(track)
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
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

    private fun playbackControl(playerProgressStatus: PlayerProgressStatus) {
        when (playerProgressStatus.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                binding.buttonPlay.setImageResource(R.drawable.button_pause)
                binding.timeOfPlay.text =
                    SimpleDateFormat(
                        "m:ss",
                        Locale.getDefault()
                    ).format(playerProgressStatus.currentPosition)
            }

            MediaPlayerStatus.STATE_PAUSED -> {
                binding.buttonPlay.setImageResource(R.drawable.button_play)
            }

            MediaPlayerStatus.STATE_PREPARED -> {
                binding.timeOfPlay.text = "0:00"
                binding.buttonPlay.setImageResource(R.drawable.button_play)
            }

            MediaPlayerStatus.STATE_ERROR -> {
                showErrorMassage()
            }

            MediaPlayerStatus.STATE_DEFAULT -> {
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
            if (!track.releaseDate.equals(getString(R.string.unknown))) track.releaseDate.substring(
                0,
                4
            ) else getString(R.string.not_found)
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

    private fun changeButtonFavoriteImage(trackAddInFavorite: Boolean) {
        if (trackAddInFavorite) {
            if (this.isNightModeOn()) binding.ivButtonFavorite.setImageResource(R.drawable.button_favorite_nm_2)
            else binding.ivButtonFavorite.setImageResource(R.drawable.button_favorite_lm_2)
        } else {
            if (this.isNightModeOn()) binding.ivButtonFavorite.setImageResource(R.drawable.button_favorite_nm_1)
            else binding.ivButtonFavorite.setImageResource(R.drawable.button_favorite_lm_1)
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

    private fun showErrorMassage() {
        Toast.makeText(
            this,
            getString(R.string.audio_file_not_available),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val TRACK_KEY = "track"
    }
}