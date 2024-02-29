package com.example.playlistmaker.presentation.ui.library

import android.provider.Settings.System.getString
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlayList

class PlayListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistImage: ImageView = itemView.findViewById(R.id.iv_playlist_image)
    private val playlistName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.tv_tracks_count)

    fun bind(model: PlayList) {
        playlistName.text = model.playlistName
        tracksCount.text = model.tracksCount.toString() + " "
    }


    private fun getWord(count: Int): String{
        if (count >= 5 && count <= 20) return getString(R.string.network_error)
    }
}



//trackName.text = model.trackName
//groupName.text = model.artistName
//trackDuration.text =
//SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
//
//Glide.with(itemView)
//.load(model.artworkUrl100)
//.placeholder(R.drawable.default_album_image)
//.centerCrop()
//.transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
//.into(albumImage)