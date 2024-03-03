package com.example.playlistmaker.presentation.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlayList

class PlayListAdapter(
    private val playLists: List<PlayList>,
    private val listener: (PlayList) -> Unit
): RecyclerView.Adapter<PlayListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playLists[position])
        holder.itemView.setOnClickListener {
            listener.invoke(playLists[position])
        }
    }
}