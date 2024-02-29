package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlayListEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "album_name")
    val albumName: String,
    @ColumnInfo(name = "album_description")
    val albumDescription: String,
    @ColumnInfo(name = "album_image")
    val albumImage: String,
    @ColumnInfo(name = "tracks_id")
    val tracksId: String,
    @ColumnInfo(name = "tracks_count")
    val tracksCount: Int
)