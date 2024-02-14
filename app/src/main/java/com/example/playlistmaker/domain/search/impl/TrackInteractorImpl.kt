package com.example.playlistmaker.domain.search.impl

import android.util.Log
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    override fun searchTracks(expression: String): Flow<TrackSearchResult> {
        return repository.searchTracks(expression)
    }
}