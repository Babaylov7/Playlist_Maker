package com.example.playlistmaker.presentation.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track

class TrackAdapterForPlaylist(
    private val tracks: List<Track>,
    private val listener: (Track) -> Unit
): RecyclerView.Adapter<TrackViewHolderForPlaylist>()  {
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
            listener.invoke(tracks[position])
        }
    }

}