package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.playlist.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun insertPlaylist(playList: PlayList)

//    suspend fun deletePlayList(id: Int)

    fun getPlayLists(): Flow<List<PlayList>>

    suspend fun updateTracksId(idPlayList: Int, tracksId: String, tracksCount: Int)
}