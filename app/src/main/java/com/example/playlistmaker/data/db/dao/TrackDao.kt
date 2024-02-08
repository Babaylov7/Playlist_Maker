package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrackFromFavorite(track: TrackEntity)

    @Query("SELECT * FROM favorite_track_table")
    suspend fun getAllFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM favorite_track_table")
    suspend fun getIdFavoriteTracks(): List<String>

}