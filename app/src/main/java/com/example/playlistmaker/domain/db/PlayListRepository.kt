package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun insertPlaylist(playList: PlayList)

    suspend fun deletePlayList(id: Int)

    fun getPlayLists(): Flow<List<PlayList>>

    suspend fun updateTracks(idPlayList: Int, tracks: ArrayList<Track>, tracksCount: Int)

    fun getPlaylist(idPlayList: Int): Flow<PlayList>

    suspend fun updatePlaylistInfo(idPlayList: Int, playlistName: String, playlistDescription: String)
}