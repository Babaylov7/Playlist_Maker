package com.example.playlistmaker.data.search.dto

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()