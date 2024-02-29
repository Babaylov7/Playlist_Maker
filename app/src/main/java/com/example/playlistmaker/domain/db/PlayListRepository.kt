package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.playlist.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun insertPlaylist(playList: PlayList)

    suspend fun deletePlayList(id: Int)

    fun getTracksId(): Flow<List<PlayList>>

    suspend fun updateTracksId(idPlayList: Int, tracksId: String, tracksCount: Int)
}