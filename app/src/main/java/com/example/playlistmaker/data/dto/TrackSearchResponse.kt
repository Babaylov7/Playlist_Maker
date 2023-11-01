package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<Track>
) : Response()