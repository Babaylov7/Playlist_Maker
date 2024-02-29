package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.db.PlayListRepository
import com.example.playlistmaker.domain.playlist.PlayList
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(val playListRepository: PlayListRepository) : PlayListInteractor {

    override suspend fun insertPlaylist(playList: PlayList) {
        playListRepository.insertPlaylist(playList)
    }

    override suspend fun deletePlayList(id: Int) {
        playListRepository.deletePlayList(id)
    }

    override fun getTracksId(): Flow<List<PlayList>> {
        return playListRepository.getTracksId()
    }

    override suspend fun updateTracksId(idPlayList: Int, tracksId: String, tracksCount: Int) {
        playListRepository.updateTracksId(idPlayList, tracksId, tracksCount)
    }
}