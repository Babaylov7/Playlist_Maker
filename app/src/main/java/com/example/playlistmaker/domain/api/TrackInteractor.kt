package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import java.util.function.Consumer

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: Consumer<List<Track>>)

//    interface TrackConsumer{
//        fun consume(foundTracks: List<Track>)
//    }
}