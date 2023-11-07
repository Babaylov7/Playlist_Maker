package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.history.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class TrackHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository):
    SearchHistoryInteractor {
    override fun addTrack(track: Track) {
        searchHistoryRepository.addTrack(track)
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return searchHistoryRepository.getTracksHistory()
    }

    override fun clean() {
        searchHistoryRepository.clean()
    }
}