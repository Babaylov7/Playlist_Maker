package com.example.playlistmaker.domain.api.history

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    fun getTracksHistory(): ArrayList<Track>
    fun clean()
}