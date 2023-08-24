package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val albumImage: ImageView = itemView.findViewById(R.id.album_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val groupName: TextView = itemView.findViewById(R.id.group_name)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)

    fun bind(model: Track){
        trackName.text = model.trackName
        groupName.text = model.groupName
        trackDuration.text = model.trackDuration

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.default_album_image)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(albumImage)

    }

}