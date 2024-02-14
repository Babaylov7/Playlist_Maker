package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {

    override suspend fun insertTrackToFavorite(track: Track) {
        favoriteTracksRepository.insertTrackToFavorite(track)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        favoriteTracksRepository.deleteTrackFromFavorite(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getAllFavoriteTracks()
    }
}