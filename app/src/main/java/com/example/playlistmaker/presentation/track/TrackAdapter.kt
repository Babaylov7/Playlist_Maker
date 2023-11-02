package com.example.playlistmaker.presentation.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.presentation.ClickListenerForRecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(
    private val tracks: List<Track>,
    private val listener: ClickListenerForRecyclerView
): RecyclerView.Adapter<TrackViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
        holder.itemView.setOnClickListener {
            listener.onClick(tracks[position])
        }
    }

}