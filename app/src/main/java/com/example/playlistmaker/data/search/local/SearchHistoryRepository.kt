package com.example.playlistmaker.data.search.local

import com.example.playlistmaker.domain.search.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getTracksHistory(): ArrayList<Track>
    fun clean()
}