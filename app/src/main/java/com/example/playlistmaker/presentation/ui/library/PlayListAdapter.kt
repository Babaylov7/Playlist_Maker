package com.example.playlistmaker.presentation.ui.library

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.playlist.PlayList

class PlayListAdapter(
    private val playLists: List<PlayList>,
    private val listener: (PlayList) -> Unit
): RecyclerView.Adapter<PlayListViewHolder> {
}