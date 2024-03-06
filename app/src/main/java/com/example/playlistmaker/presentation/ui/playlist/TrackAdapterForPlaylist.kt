package com.example.playlistmaker.presentation.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track

class TrackAdapterForPlaylist(
    private val tracks: List<Track>
): RecyclerView.Adapter<TrackViewHolderForPlaylist>()  {

    var itemClickListener: ((Int, Track) -> Unit)? = null
    var itemLongClickListener: ((Int, Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolderForPlaylist {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolderForPlaylist(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolderForPlaylist, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position, tracks[position])
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            itemLongClickListener?.invoke(position, tracks[position])
            return@OnLongClickListener true
        })
    }

}