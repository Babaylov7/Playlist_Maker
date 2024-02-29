package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.converters.PlayListDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlayListRepository
import com.example.playlistmaker.domain.playlist.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListDbConvertor: PlayListDbConvertor
): PlayListRepository {
    override suspend fun insertPlaylist(playList: PlayList) {
        val playListEntity = playListDbConvertor.map(playList)
        appDatabase.playListDao().insertPlaylist(playListEntity)
    }

    override suspend fun deletePlayList(id: Int) {
        appDatabase.playListDao().deletePlayList(id)
    }

    override fun getTracksId(): Flow<List<PlayList>> = flow {
        val playLists = appDatabase.playListDao().getTracksId()
        emit(convertFromPlayListEntity(playLists))
    }

    override suspend fun updateTracksId(idPlayList: Int, tracksId: String, tracksCount: Int) {
        appDatabase.playListDao().updateTracksId(idPlayList, tracksId, tracksCount)
    }

    private fun convertFromPlayListEntity(playLists: List<PlayListEntity>) : List<PlayList> {
        return playLists.map { playList ->  playListDbConvertor.map(playList)}
    }
}