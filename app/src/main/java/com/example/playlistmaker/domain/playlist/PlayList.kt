package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.search.models.Track


data class PlayList(
    val id: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistImage: String?,
    val tracks: ArrayList<Track>,
    val tracksCount: Int
)



