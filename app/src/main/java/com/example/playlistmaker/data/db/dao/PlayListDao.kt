package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlayListEntity

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)                //Добавить плейлист
    suspend fun insertPlaylist(playlist: PlayListEntity)

//    @Query("DELETE FROM playlists_table WHERE id = :idPlayList")   //Удалить плейлист по id
//    suspend fun deletePlayList(idPlayList: Int)

    @Query("SELECT * FROM playlists_table")      //Получить сущность
    suspend fun getPlayLists(): List<PlayListEntity>

    @Query("UPDATE playlists_table SET tracks_id = :tracksId, tracks_count = :tracksCount  WHERE id = :idPlayList")     //Обновить список треков
    suspend fun updateTracks(idPlayList: Int, tracksId: String, tracksCount: Int)

    @Query("SELECT * FROM playlists_table WHERE id = :idPlayList")
    suspend fun getPlaylist(idPlayList: Int): PlayListEntity


}