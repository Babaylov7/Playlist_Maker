package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.db.PlayListRepository
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(val playListRepository: PlayListRepository) : PlayListInteractor {

    override suspend fun insertPlaylist(playList: PlayList) {
        playListRepository.insertPlaylist(playList)
    }

    override suspend fun deletePlayList(id: Int) {
        playListRepository.deletePlayList(id)
    }

    override fun getPlayLists(): Flow<List<PlayList>> {
        return playListRepository.getPlayLists()
    }

    override suspend fun updateTracks(idPlayList: Int, tracks: ArrayList<Track>, tracksCount: Int) {
        playListRepository.updateTracks(idPlayList, tracks, tracksCount)
    }

    override fun getPlaylist(idPlayList: Int): Flow<PlayList> {
        return playListRepository.getPlaylist(idPlayList)
    }
}