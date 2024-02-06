package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.TrackSearchResult
import kotlinx.coroutines.flow.Flow
import java.util.function.Consumer

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<TrackSearchResult>
}