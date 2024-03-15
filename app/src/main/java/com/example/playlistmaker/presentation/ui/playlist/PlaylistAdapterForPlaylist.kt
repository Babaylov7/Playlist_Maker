package com.example.playlistmaker.presentation.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.models.PlayList

class PlaylistAdapterForPlaylist (
    private val playLists: List<PlayList>
): RecyclerView.Adapter<PlaylistViewHolderForPlaylist>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolderForPlaylist {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_to_botton_sheet, parent, false)
        return PlaylistViewHolderForPlaylist(view)
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolderForPlaylist, position: Int) {
        holder.bind(playLists[position])
    }
}