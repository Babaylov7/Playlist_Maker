package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.TrackSearchResult

interface TrackRepository {
    fun searchTracks(expression: String): TrackSearchResult
}