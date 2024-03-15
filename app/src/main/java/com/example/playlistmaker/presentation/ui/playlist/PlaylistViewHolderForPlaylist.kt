package com.example.playlistmaker.presentation.ui.playlist

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.PlayList
import java.io.File

class PlaylistViewHolderForPlaylist (itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistImage: ImageView = itemView.findViewById(R.id.iv_playlist_image)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tv_tracks_count)

    fun bind(model: PlayList) {
        playlistName.text = model.playlistName
        tracksCount.text = model.tracksCount.toString() + " " + itemView.context.resources.getQuantityString(R.plurals.plurals_track, model.tracksCount)

        val filePath =
            File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

        Glide.with(itemView)
            .load(model.playlistImage?.let { playlistImage -> File(filePath, playlistImage) })
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(playlistImage)
    }

    companion object {
        private const val DIRECTORY = "album_images"
    }
}