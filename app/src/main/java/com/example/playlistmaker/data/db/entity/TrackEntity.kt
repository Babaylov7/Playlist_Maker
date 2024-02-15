package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_track_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: String,
    @ColumnInfo(name = "track_name")
    val trackName: String,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "track_time_millis")
    val trackTimeMillis: Long,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl100: String,
    @ColumnInfo(name = "collection_name")
    val collectionName: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "genre")
    val primaryGenreName: String,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
)