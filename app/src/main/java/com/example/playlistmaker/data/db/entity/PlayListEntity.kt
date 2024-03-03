package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlayListEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "playList_name")
    val playListName: String,
    @ColumnInfo(name = "playList_description")
    val playListDescription: String,
    @ColumnInfo(name = "playList_image")
    val playListImage: String?,
    @ColumnInfo(name = "tracks_id")
    val tracksId: String?,
    @ColumnInfo(name = "tracks_count")
    val tracksCount: Int
)