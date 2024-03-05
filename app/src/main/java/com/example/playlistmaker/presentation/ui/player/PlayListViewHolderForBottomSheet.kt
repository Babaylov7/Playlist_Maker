package com.example.playlistmaker.presentation.ui.player

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlayList
import java.io.File

class PlayListViewHolderForBottomSheet (itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistImage: ImageView = itemView.findViewById(R.id.iv_playlist_image)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tv_tracks_count)

    fun bind(model: PlayList) {
        playlistName.text = model.playlistName
        tracksCount.text = model.tracksCount.toString() + " " + getWord(model.tracksCount)

        val filePath =
            File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY)

        Glide.with(itemView)
            .load(model.playlistImage?.let { playlistImage -> File(filePath, playlistImage) })
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(playlistImage)
    }


    private fun getWord(count: Int): String {
        if (count == 0) return itemView.context.getString(R.string.more_that_one_track_2)
        if ((count >= 5) && (count <= 20)) return itemView.context.getString(R.string.more_that_one_track_2)
        if (count % 10 == 1) return itemView.context.getString(R.string.one_track)
        return itemView.context.getString(R.string.more_that_one_track_1)
    }

    companion object {
        private const val DIRECTORY = "album_images"
    }
}