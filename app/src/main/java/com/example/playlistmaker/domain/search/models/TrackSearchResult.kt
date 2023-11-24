package com.example.playlistmaker.domain.search.models

import com.example.playlistmaker.domain.search.Response

data class TrackSearchResult(
    var results: List<Track>
) : Response()