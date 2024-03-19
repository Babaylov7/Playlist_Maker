package com.example.playlistmaker.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.search.models.Track

class TrackListDiffUtilCallback(val oldList: List<Track>, val newList: List<Track>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldList[oldItemPosition]
        val newTrack = newList[newItemPosition]
        return oldTrack.trackId == newTrack.trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        val oldTrack = oldList[oldItemPosition]
//        val newTrack = newList[newItemPosition]
//        return oldTrack.trackName.equals(newTrack.trackName) && oldTrack.artistName.equals(newTrack.artistName) && (oldTrack.trackTimeMillis == newTrack.trackTimeMillis)
        return true
    }
}