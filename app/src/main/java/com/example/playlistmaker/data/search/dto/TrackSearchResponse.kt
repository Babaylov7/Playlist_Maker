package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.domain.search.Response

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()