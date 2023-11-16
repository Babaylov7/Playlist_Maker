package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.domain.search.models.TrackSearchResult

interface TrackRepository {
    fun searchTracks(expression: String): TrackSearchResult
}