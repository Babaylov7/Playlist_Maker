package com.example.playlistmaker.domain.search.models


data class TrackSearchResult(
    var results: List<Track>,
    var status: SearchStatus
)