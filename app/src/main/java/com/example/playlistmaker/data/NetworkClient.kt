package com.example.playlistmaker.data

import com.example.playlistmaker.domain.search.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest

interface NetworkClient {
    fun doTrackSearchRequest(dto: TrackSearchRequest): Response
}