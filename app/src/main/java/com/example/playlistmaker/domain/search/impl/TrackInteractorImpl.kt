package com.example.playlistmaker.domain.search.impl

import android.util.Log
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors
import java.util.function.Consumer

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    override fun searchTracks(expression: String): Flow<TrackSearchResult> {
        Log.d("MY_TAG_44444", "TrackInteractorImpl запустили метод searchTracks")
        return repository.searchTracks(expression)
    }
}