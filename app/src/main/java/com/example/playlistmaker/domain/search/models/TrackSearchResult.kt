package com.example.playlistmaker.domain.search.models

import com.example.playlistmaker.data.search.dto.Response

data class TrackSearchResult(
    var results: List<Track>
) : Response()