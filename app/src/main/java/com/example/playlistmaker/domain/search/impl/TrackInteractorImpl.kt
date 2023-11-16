package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.models.TrackSearchResult
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.data.search.network.TrackRepository
import java.util.concurrent.Executors
import java.util.function.Consumer

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: Consumer<TrackSearchResult>) {
        executor.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }
}