package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.converters.PlayListDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlayListRepository
import com.example.playlistmaker.domain.playlist.PlayList
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
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

//    override suspend fun deletePlayList(id: Int) {
//        appDatabase.playListDao().deletePlayList(id)
//    }

    override fun getPlayLists(): Flow<List<PlayList>> = flow {
        val playLists = appDatabase.playListDao().getPlayLists()
        emit(convertFromPlayListEntity(playLists))
    }

    override suspend fun updateTracks(idPlayList: Int, tracks: ArrayList<Track>, tracksCount: Int) {
        val tracksForDb = createJsonFromTracks(tracks)
        appDatabase.playListDao().updateTracks(idPlayList, tracksForDb, tracksCount)
    }

    private fun convertFromPlayListEntity(playLists: List<PlayListEntity>) : List<PlayList> {
        return playLists.map { playList ->  playListDbConvertor.map(playList)}
    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }
}