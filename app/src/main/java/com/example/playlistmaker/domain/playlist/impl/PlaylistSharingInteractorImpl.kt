package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.playlist.PlaylistSharingInteractor
import com.example.playlistmaker.domain.playlist.PlaylistSharingRepository

class PlaylistSharingInteractorImpl(private val repository: PlaylistSharingRepository):
    PlaylistSharingInteractor {
    override fun shareTrackList(playlistInfo: String) {
        repository.shareTrackList(playlistInfo)
    }

}