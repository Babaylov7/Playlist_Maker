package com.example.playlistmaker

data class NetworkQueryResult(
    val searchStatus: SearchStatus,
    val tracks: ArrayList<Track>
)
