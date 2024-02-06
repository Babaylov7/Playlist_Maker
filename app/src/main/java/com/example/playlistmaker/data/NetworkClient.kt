package com.example.playlistmaker.data

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest

interface NetworkClient {
    suspend fun doTrackSearchRequest(dto: TrackSearchRequest): Response
}