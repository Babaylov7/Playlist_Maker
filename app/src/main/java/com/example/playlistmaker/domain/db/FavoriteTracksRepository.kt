package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    fun insertTrackToFavorite(track: Track)

    fun deleteTrackFromFavorite(track: Track)

    fun getAllFavoriteTracks(): Flow<List<Track>>
}