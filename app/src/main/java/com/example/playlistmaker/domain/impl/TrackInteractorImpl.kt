package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.track.TrackInteractor
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors
import java.util.function.Consumer

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }
}