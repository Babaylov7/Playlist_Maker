package com.example.playlistmaker.domain.playlist



data class PlayList(
    val id: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistImage: String,
    val tracksId: String,
    val tracksCount: Int
)



