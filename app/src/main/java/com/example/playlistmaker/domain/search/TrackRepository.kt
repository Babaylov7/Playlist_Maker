package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.TrackSearchResult
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<TrackSearchResult>
}