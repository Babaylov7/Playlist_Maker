package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository):
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