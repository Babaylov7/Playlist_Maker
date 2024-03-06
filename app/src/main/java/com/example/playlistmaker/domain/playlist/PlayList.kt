package com.example.playlistmaker.domain.playlist

import android.os.Parcelable
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayList(
    val id: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistImage: String?,
    val tracks: ArrayList<Track>,
    val tracksCount: Int
): Parcelable



