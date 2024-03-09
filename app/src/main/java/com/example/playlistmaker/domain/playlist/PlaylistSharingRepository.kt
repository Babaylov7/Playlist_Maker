package com.example.playlistmaker.domain.playlist

interface PlaylistSharingRepository {
    fun shareTrackList(playlistInfo: String)
}