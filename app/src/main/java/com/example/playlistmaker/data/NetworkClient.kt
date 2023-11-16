package com.example.playlistmaker.data

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest

interface NetworkClient {
    fun doTrackSearchRequest(dto: TrackSearchRequest): Response
}