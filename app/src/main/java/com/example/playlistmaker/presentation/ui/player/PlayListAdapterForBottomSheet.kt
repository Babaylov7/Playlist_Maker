package com.example.playlistmaker.presentation.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlist.PlayList
import com.example.playlistmaker.presentation.ui.library.PlayListViewHolder

class PlayListAdapterForBottomSheet(
    private val playLists: List<PlayList>,
    private val listener: (PlayList) -> Unit
): RecyclerView.Adapter<PlayListViewHolderForBottomSheet>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolderForBottomSheet {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_to_botton_sheet, parent, false)
        return PlayListViewHolderForBottomSheet(view)
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolderForBottomSheet, position: Int) {
        holder.bind(playLists[position])
    }
}