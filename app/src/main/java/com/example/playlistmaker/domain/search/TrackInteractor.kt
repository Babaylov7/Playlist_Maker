package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.TrackSearchResult
import java.util.function.Consumer

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TrackSearchResult>)

}